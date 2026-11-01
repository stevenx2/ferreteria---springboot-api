package com.johan.ferreteria.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.johan.ferreteria.dto.SupplierRequestDTO;
import com.johan.ferreteria.dto.SupplierResponseDTO;
import com.johan.ferreteria.entities.Supplier;

/**
 * 
 * SupplierService
 * firma de métodos para la implementación del servicio de la entidad supplier
 */
public interface SupplierService {


    /**
     * función de utilidad para métodos del servicio que necesiten retornar el DTO de la respuesta en lugar de la entidad propia (siempre en mis métodos)
     * @param supplier
     * @return
     */
    default SupplierResponseDTO mapEntityToResponseDto(Supplier supplier) {
        SupplierResponseDTO response = new SupplierResponseDTO();
        response.setAddress(supplier.getAddress());
        response.setName(supplier.getName());
        response.setPhone_number(supplier.getPhone_number());
        response.setSupplier_id(supplier.getId());
        return response;
    }
    

    List<SupplierResponseDTO> findAll();

    Page<SupplierResponseDTO> findAllByPage(Pageable pageable);

    SupplierResponseDTO findById(Long id);

    SupplierResponseDTO update(Long id, SupplierRequestDTO supplier);

    SupplierResponseDTO save(SupplierRequestDTO supplier);

    List<SupplierResponseDTO> findByNameContaining(String name);

}
