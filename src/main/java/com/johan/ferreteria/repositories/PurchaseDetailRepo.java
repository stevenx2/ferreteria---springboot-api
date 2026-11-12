package com.johan.ferreteria.repositories;

import com.johan.ferreteria.entities.PurchaseDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.nio.file.LinkOption;

public interface PurchaseDetailRepo extends JpaRepository<PurchaseDetail, LinkOption> {


    //buscar los detalles por su identificador de compra
    Iterable<PurchaseDetail> findByPurchase_Id(Long id);
}
