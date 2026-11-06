package com.johan.ferreteria.services;

import com.johan.ferreteria.dto.SaleDetailRequestDTO;
import com.johan.ferreteria.dto.SaleDetailResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Reglas del servicio de los detalles de venta (SaleDetail)
 */
public interface SaleDetailService {

    List<SaleDetailResponseDTO> findAll();
    Page<SaleDetailResponseDTO> findAllByPage(Pageable pageable);
    List<SaleDetailResponseDTO> findByUnitPriceBetween(float minPrice, float maxPrice);
    List<SaleDetailResponseDTO> findBySaleId(Long id);

    SaleDetailResponseDTO findById(Long id);
    void deleteById(Long id);

    SaleDetailResponseDTO save(SaleDetailRequestDTO saleDetail);
}
