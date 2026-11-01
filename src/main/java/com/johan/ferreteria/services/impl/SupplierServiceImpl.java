package com.johan.ferreteria.services.impl;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.johan.ferreteria.dto.SupplierRequestDTO;
import com.johan.ferreteria.dto.SupplierResponseDTO;
import com.johan.ferreteria.entities.Supplier;
import com.johan.ferreteria.repositories.SupplierRepo;
import com.johan.ferreteria.services.SupplierService;
import com.johan.problem.ResourceNotFoundException;

/**
 * 
 * SupplierServiceImpl
 * Implementación del servicio para la entidad supplier
 */
@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepo supplierRepo;
    private final MessageSource messageSource;

    public SupplierServiceImpl(SupplierRepo supplierRepo, MessageSource messageSource) {
        this.supplierRepo = supplierRepo;
        this.messageSource = messageSource;
    }

    @Transactional(readOnly = true)
    @Override
    public List<SupplierResponseDTO> findAll() {
        return supplierRepo.findAll()
                .stream().map(this::mapEntityToResponseDto)
                .toList();
    }

    @Transactional (readOnly = true)
    @Override
    public Page<SupplierResponseDTO> findAllByPage(Pageable pageable) {
        return supplierRepo.findAll(pageable).map(this::mapEntityToResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public SupplierResponseDTO findById(Long id) {
        Supplier found = supplierRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("resource.notfound", new Object[] { id },
                                LocaleContextHolder.getLocale())));

        return this.mapEntityToResponseDto(found);
    }

    @Transactional(readOnly = true)
    @Override
    public List<SupplierResponseDTO> findByNameContaining(String name) {

        return StreamSupport
                .stream(supplierRepo.findByNameContaining(name).spliterator(), false)
                .map(this::mapEntityToResponseDto)
                .toList();
    }

    @Override
    public SupplierResponseDTO save(SupplierRequestDTO supplier) {
        Supplier newSupplier = new Supplier();
        newSupplier.setAddress(supplier.getAddress());
        newSupplier.setName(supplier.getName());
        newSupplier.setPhone_number(supplier.getPhone_number());

        Supplier saved = supplierRepo.save(newSupplier);
        return mapEntityToResponseDto(saved);
    }

    @Override
    public SupplierResponseDTO update(Long id, SupplierRequestDTO supplier) {
        Supplier found = supplierRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("resource.notfound", new Object[] { id },
                                LocaleContextHolder.getLocale())));

        found.setAddress(supplier.getAddress());
        found.setName(supplier.getName());
        found.setPhone_number(supplier.getPhone_number());

        Supplier updated = supplierRepo.save(found);
        return this.mapEntityToResponseDto(updated);
    }

}
