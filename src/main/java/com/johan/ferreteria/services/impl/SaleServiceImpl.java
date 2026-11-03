package com.johan.ferreteria.services.impl;

import com.johan.ferreteria.dto.SaleDetailResponseDTO;
import com.johan.ferreteria.dto.SaleRequestDTO;
import com.johan.ferreteria.dto.SaleResponseDTO;
import com.johan.ferreteria.entities.Client;
import com.johan.ferreteria.entities.Sale;
import com.johan.ferreteria.entities.SaleDetail;
import com.johan.ferreteria.problem.ResourceNotFoundException;
import com.johan.ferreteria.repositories.ClientRepo;
import com.johan.ferreteria.repositories.SaleDetailRepo;
import com.johan.ferreteria.repositories.SaleRepo;
import com.johan.ferreteria.services.SaleService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

/**
 * servicio de las la entidad Sale (venta de productos a clientes/compras de productos por parte de clientes)
 */
@Transactional
@Service
public class SaleServiceImpl implements SaleService {


    private final SaleRepo saleRepo;
    private final ClientRepo clientRepo;
    private final SaleDetailRepo saleDetailRepo;
    private final MessageSource messageSource;


    public SaleServiceImpl(SaleRepo saleRepo, MessageSource messageSource, ClientRepo clientRepo, SaleDetailRepo saleDetailRepo) {
        this.saleRepo = saleRepo;
        this.messageSource = messageSource;
        this.clientRepo = clientRepo;
        this.saleDetailRepo = saleDetailRepo;
    }

    @Transactional(readOnly = true)
    @Override
    public List<SaleResponseDTO> findAll() {
        return saleRepo.findAll()
                .stream()
                .map(this::mapToSaleResponseDto)
                .toList();
    }


    @Transactional(readOnly = true)
    @Override
    public Page<SaleResponseDTO> findAllByPage(Pageable pageable) {
        return saleRepo.findAll(pageable)
                .map(this::mapToSaleResponseDto);
    }


    /**
     * buscar todas las compras realizadas por un cliente
     *
     * @param id identificador del cliente
     * @return una lista de tipo SaleResponseDTO con todas las compras de un cliente
     */
    @Transactional(readOnly = true)
    @Override
    public List<SaleResponseDTO> findByClientId(Long id) {
        return StreamSupport.stream(
                        saleRepo.findByClient_Id(id).spliterator(),
                        false
                )
                .map(this::mapToSaleResponseDto) //mapeo los objetos a una lista de tipo SaleResponseDTO
                .toList();
    }

    /**
     * retorna todas las compras realizas por un cliente
     *
     * @param name nombre del cliente. El nombre debe ser exacto, contando mayúsculas y minúsculas
     * @return una lista de tipo SaleResponseDTO con todas las compras de un cliente
     */
    @Transactional(readOnly = true)
    @Override
    public List<SaleResponseDTO> findByClientName(String name) {
        return StreamSupport.stream(
                        saleRepo.findByClient_Name(name).spliterator(),
                        false
                )
                .map(this::mapToSaleResponseDto) //mapeo los objetos a una lista de tipo SaleResponseDTO
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public SaleResponseDTO findById(Long id) {
        Sale found = saleRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("resource.notfound", new Object[]{id}, LocaleContextHolder.getLocale())
                ));

        return this.mapToSaleResponseDto(found);
    }

    @Override
    public SaleResponseDTO save(SaleRequestDTO sale) {

        //busco al cliente que se le quiere asignar la venta (en la perspectiva del cliente sería lo que él compra)
        Client foundClient = clientRepo.findById(sale.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("sale.clientId.notFound", new Object[]{sale.getClientId()}, LocaleContextHolder.getLocale())
                ));


        //creo el objeto para guardar en la base de datos
        Sale newSale = new Sale();
        newSale.setClient(foundClient);

        //guardo la venta
        Sale saved = saleRepo.save(newSale);

        //mapeo del objeto retornado a su respectivo DTO
        return this.mapToSaleResponseDto(saved);
    }


    @Override
    public SaleResponseDTO update(Long id, SaleRequestDTO sale) {

        //busco la venta en la base de datos o lanzo encepción si no se encuentra
        Sale saleFromDB = saleRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("sale.update.notFound", new Object[]{id}, LocaleContextHolder.getLocale())
                ));


        //busco al cliente que se le quiere asignar la venta (en la perspectiva del cliente sería lo que él compra)
        Client newClient = clientRepo.findById(sale.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("sale.clientId.notFound", new Object[]{sale.getClientId()}, LocaleContextHolder.getLocale())
                ));


        /**
         * si el nuevo cliente que se le quiere asignar a esta venta es distinto al anterior, actualizo la venta
         * y retorno el nuevo objeto
         */
        Client oldClient = saleFromDB.getClient();
        if (!oldClient.equals(newClient)) {
            saleFromDB.setClient(newClient); //asigno el nuevo cliente
            Sale updated = saleRepo.save(saleFromDB);  //actualizo la venta
            return this.mapToSaleResponseDto(updated); // retorno la venta actualizada
        }

        // si no se cambió nada de la venta,entonces retorno el objeto anterior sin actualizar
        return this.mapToSaleResponseDto(saleFromDB);
    }


    /**
     * mapea un objeto de tipo Sale a uno de tipo SaleResponseDTO, que esel DTO de respuestas en la api para este objeto.
     *
     * @param sale el objeto proveniente de la base de datos que representa a una venta
     * @return un objeto de tipo SaleResponseDTO que tienen los campos de Sale que quiere exponer al cliente
     * @see com.johan.ferreteria.dto.SaleResponseDTO
     * @see com.johan.ferreteria.entities.Sale
     */
    private SaleResponseDTO mapToSaleResponseDto(Sale sale) {
        SaleResponseDTO maped = new SaleResponseDTO();
        maped.setId(sale.getId());
        maped.setClientId(sale.getClient().getId());
        maped.setDate(sale.getDate());


        //Sumo el precio unitario de cada detalle para obtener el precio total de la venta y luego lo asigno al objeto
        double totalPrice = StreamSupport.stream(
                        saleDetailRepo.findBySale_Id(sale.getId()).spliterator(),
                        false
                )
                .mapToDouble(detail ->
                        detail.getUnit_price() * detail.getQuantity() // multiplico el precio unitario de cada producto más la cantidad comprada
                )
                .sum();

        maped.setTotalPrice(totalPrice);

        return maped;
    }
}
