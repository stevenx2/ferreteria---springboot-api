package com.johan.ferreteria.services;

import com.johan.ferreteria.dto.SaleRequestDTO;
import com.johan.ferreteria.dto.SaleResponseDTO;
import com.johan.ferreteria.entities.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Reglas de los servicios de la entidad Sale
 */
public interface SaleService {


    List<SaleResponseDTO> findAll();
    Page<SaleResponseDTO> findAllByPage(Pageable pageable);
    List<SaleResponseDTO> findByClientId(Long id);
    List<SaleResponseDTO> findByClientName(String name);

    SaleResponseDTO findById(Long id);

    SaleResponseDTO save(SaleRequestDTO sale);

    SaleResponseDTO update(Long id,SaleRequestDTO sale);

}
