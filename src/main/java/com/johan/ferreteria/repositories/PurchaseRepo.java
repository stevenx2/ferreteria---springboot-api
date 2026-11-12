package com.johan.ferreteria.repositories;

import com.johan.ferreteria.entities.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepo extends JpaRepository<Purchase,Long> {

    Iterable<Purchase> findBySupplier_Name(String name);
    Iterable<Purchase> findBySupplier_Id(Long id);
    boolean existsBySupplier_Name(String name); // para saber si existe por lo menos una compra a ese proveedor por su nombre
    boolean existsBySupplier_Id(Long id); // lo mismo que el método de arriba pero con el id del proveedor
}
