package com.johan.ferreteria.services.impl;

import com.johan.ferreteria.dto.SaleDetailRequestDTO;
import com.johan.ferreteria.dto.SaleDetailResponseDTO;
import com.johan.ferreteria.entities.Product;
import com.johan.ferreteria.entities.Sale;
import com.johan.ferreteria.entities.SaleDetail;
import com.johan.ferreteria.problem.ResourceNotFoundException;
import com.johan.ferreteria.repositories.ProductRepo;
import com.johan.ferreteria.repositories.SaleDetailRepo;
import com.johan.ferreteria.repositories.SaleRepo;
import com.johan.ferreteria.services.SaleDetailService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class SaleDetailServiceImpl implements SaleDetailService {

    private final SaleDetailRepo saleDetailRepo;
    private final MessageSource messageSource;
    private final SaleRepo saleRepo;
    private final ProductRepo productRepo;

    public SaleDetailServiceImpl(SaleDetailRepo saleDetailRepo, MessageSource messageSource, SaleRepo saleRepo, ProductRepo productRepo) {
        this.saleDetailRepo = saleDetailRepo;
        this.messageSource = messageSource;
        this.saleRepo = saleRepo;
        this.productRepo = productRepo;
    }

    @Transactional(readOnly = true)
    @Override
    public List<SaleDetailResponseDTO> findAll() {
        return saleDetailRepo.findAll()
                .stream()
                .map(this::mapEntityToResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<SaleDetailResponseDTO> findAllByPage(Pageable pageable) {
        return saleDetailRepo.findAll(pageable).map(this::mapEntityToResponseDto);
    }

    /**
     * Encuentra todos los detalles de venta que extén en un rango de precios (contando los extremos)
     *
     * @param minPrice el precio mínimo de los detalles a devolver
     * @param maxPrice el precio máximo de los detalles a devolver
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public List<SaleDetailResponseDTO> findByUnitPriceBetween(float minPrice, float maxPrice) {
        return StreamSupport.stream(
                saleDetailRepo.findByUnitPriceBetween(minPrice, maxPrice).spliterator(),
                false
        ).map(this::mapEntityToResponseDto).toList();
    }


    /**
     * Encuentra todos los detalles de venta mediante el identificador de la venta
     *
     * @param id identificador de la venta (Sale)
     * @return una lista con todos los detalles de esa venta
     */
    @Transactional(readOnly = true)
    @Override
    public List<SaleDetailResponseDTO> findBySaleId(Long id) {
        return StreamSupport.stream(
                saleDetailRepo.findBySale_Id(id).spliterator(),
                false
        ).map(this::mapEntityToResponseDto).toList();
    }

    @Override
    public SaleDetailResponseDTO findById(Long id) {
        //encuentro el registro o lanzo excepción si no existe
        SaleDetail saleDetail = saleDetailRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("resource.notfound", new Object[]{id}, LocaleContextHolder.getLocale())
                ));

        //mapeo el registro al DTO d
        return this.mapEntityToResponseDto(saleDetail);
    }

    @Override
    public void deleteById(Long id) {
        if (!saleDetailRepo.existsById(id)) {
            throw new ResourceNotFoundException(
                    messageSource.getMessage("resource.notfound", new Object[]{id}, LocaleContextHolder.getLocale())
            );
        }

        saleDetailRepo.deleteById(id);
    }

    @Override
    public SaleDetailResponseDTO save(SaleDetailRequestDTO saleDetail) {

        //Busco la venta en la base de datos o lanzo excepción si no existe
        Sale sale = saleRepo.findById(saleDetail.getSaleId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                messageSource.getMessage("sale.saleId.notFound", new Object[]{saleDetail.getSaleId()}, LocaleContextHolder.getLocale())
                        )
                );


        //Busco el producto en la base de datos o lanzo excepción si no existe
        Product product = productRepo.findById(saleDetail.getProductId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                messageSource.getMessage("sale.productId.notFound", new Object[]{saleDetail.getProductId()}, LocaleContextHolder.getLocale())
                        )
                );


        // creo el objeto que voy a guardar en la base de datos
        SaleDetail detail = new SaleDetail();
        detail.setSale(sale);
        detail.setProduct(product);
        detail.setQuantity(saleDetail.getQuantity());
        detail.setUnitPrice(product.getPrice()); // el precio unitario del detalle es el precio actual del producto

        //guardao el detalle
        SaleDetail saved = saleDetailRepo.save(detail);

        //mapeo el objeto a su clase DTO de respuestas y lo retorno
        return this.mapEntityToResponseDto(saved);
    }


    /**
     * Mapea la clase de la entidad SaleDetail a su clase DTO de respuestas. En la clase DTO solo expongo los campos que quiero.
     * Este método se usa en todas las funciones que necesitan retornar el nuevo objeto
     *
     * @param saleDetail la instancia de la entidad proveniente de la base de datos
     * @return Un objeto mapeado a su clase DTO
     */
    private SaleDetailResponseDTO mapEntityToResponseDto(SaleDetail saleDetail) {
        return new SaleDetailResponseDTO(
                saleDetail.getId(),
                saleDetail.getSale().getId(),
                saleDetail.getProduct().getId(),
                saleDetail.getQuantity(),
                saleDetail.getUnitPrice());
    }
}
