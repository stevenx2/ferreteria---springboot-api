package com.johan.ferreteria.repositories;

import com.johan.ferreteria.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepo extends JpaRepository<Sale,Long> {

    //buscar ventas por el identificador de cliente
    Iterable<Sale> findByClient_Id(Long id);

    //buscar ventas por el nombre del cliente
    Iterable<Sale> findByClient_Name(String name);
}
