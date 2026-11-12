package com.johan.ferreteria.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.johan.ferreteria.entities.Supplier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SupplierRepo extends JpaRepository<Supplier, Long> {
    
    Iterable<Supplier> findByName(String name);
    Iterable<Supplier> findByNameContaining(String name);

    boolean existsByName(String name);

    /**
     * Obtengo solo el nombre del proveedor mediante su id. Esto lo uso para unos mensajes de error
     * cuando el proveedor no tiene ninguna venta a la ferreteria.
     * @param id el identificador del proveedor
     * @return un Optional de tipo String con el nombre del proveedor
     */
    @Query("SELECT s.name FROM Supplier s WHERE s.id = :id")
    Optional<String> findSupplierNameById(@Param("id") Long id);

}
