package com.johan.ferreteria.services.impl;

import com.johan.ferreteria.dto.SaleRequestDTO;
import com.johan.ferreteria.dto.SaleResponseDTO;
import com.johan.ferreteria.entities.Client;
import com.johan.ferreteria.entities.Sale;
import com.johan.ferreteria.problem.ResourceNotFoundException;
import com.johan.ferreteria.repositories.ClientRepo;
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
    private final MessageSource messageSource;


    public SaleServiceImpl(SaleRepo saleRepo, MessageSource messageSource, ClientRepo clientRepo) {
        this.saleRepo = saleRepo;
        this.messageSource = messageSource;
        this.clientRepo = clientRepo;
    }

    @Transactional(readOnly = true)
    @Override
    public List<SaleResponseDTO> findAll() {
        return saleRepo.findAll()
                .stream()
                .map(this::mapEntityToResponseDto)
                .toList();
    }


    @Transactional(readOnly = true)
    @Override
    public Page<SaleResponseDTO> findAllByPage(Pageable pageable) {
        return saleRepo.findAll(pageable)
                .map(this::mapEntityToResponseDto);
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
                .map(this::mapEntityToResponseDto) //mapeo los objetos a una lista de tipo SaleResponseDTO
                .toList();
    }

    /**
     * retorna todas las compras realizas por un cliente
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
                .map(this::mapEntityToResponseDto) //mapeo los objetos a una lista de tipo SaleResponseDTO
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public SaleResponseDTO findById(Long id) {
        Sale found = saleRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("resource.notfound", new Object[]{id}, LocaleContextHolder.getLocale())
                ));

        return this.mapEntityToResponseDto(found);
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
        return this.mapEntityToResponseDto(saved);
    }



    @Override
    public SaleResponseDTO update(Long id,SaleRequestDTO sale) {

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
        if(!oldClient.equals(newClient)){
            saleFromDB.setClient(newClient); //asigno el nuevo cliente
            Sale updated = saleRepo.save(saleFromDB);  //actualizo la venta
            return this.mapEntityToResponseDto(updated); // retorno la venta actualizada
        }

        // si no se cambió nada de la venta,entonces retorno el objeto anterior sin actualizar
        return this.mapEntityToResponseDto(saleFromDB);
    }
}
