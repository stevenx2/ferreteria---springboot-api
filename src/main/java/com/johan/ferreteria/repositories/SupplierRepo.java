package com.johan.ferreteria.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.johan.ferreteria.entities.Supplier;

public interface SupplierRepo extends JpaRepository<Supplier, Long> {
    
    Iterable<Supplier> findByName(String name);
    Iterable<Supplier> findByNameContaining(String name);

    
}
