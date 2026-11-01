package com.johan.ferreteria.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.johan.ferreteria.entities.Product;

public interface ProductRepo extends JpaRepository<Product, Long> {
    
    Iterable<Product> findByNameContaining(String name);

    // 1. Precio menor a (Less Than)
    Iterable<Product> findByPriceLessThan(double price);

    // 2. Precio mayor a (Greater Than)
    Iterable<Product> findByPriceGreaterThan(double price);

    // 3. Precio entre dos valores (Between)
    Iterable<Product> findByPriceBetween(double minPrice, double maxPrice);

    // Obtener todos los productos de un proveedor. La clase DTO del proveedor no expone los productos, por lo que este método puede ser útil
    Iterable<Product> findBySupplier_Id(Long id);

}
