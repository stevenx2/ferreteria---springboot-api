package com.johan.ferreteria.services;

import com.johan.ferreteria.dto.PurchaseRequestDTO;
import com.johan.ferreteria.dto.PurchaseResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Reglas del servicio de compras por parte de la ferreteria a los proveedores (Purchase)
 */
public interface PurchaseService {

    List<PurchaseResponseDTO> findAll();
    Page<PurchaseResponseDTO> findAllByPage(Pageable pageable);
    List<PurchaseResponseDTO> findAllBySupplierName(String supplierName);
    List<PurchaseResponseDTO> findAllBySupplierId(Long id);

    PurchaseResponseDTO findById(Long id);
    PurchaseResponseDTO update(Long id,PurchaseRequestDTO purchase);

    PurchaseResponseDTO save(PurchaseRequestDTO purchase);
}
