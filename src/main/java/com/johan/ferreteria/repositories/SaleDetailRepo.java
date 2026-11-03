package com.johan.ferreteria.repositories;

import com.johan.ferreteria.entities.SaleDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleDetailRepo extends JpaRepository<SaleDetail,Long> {

    /**
     * Buscar todos los detalles de una venta. Se usa principalmente para sumar los precios de todos los detalles
     * y obtener el precio total, el cual uso en el campo totalPrice de SaleResponseDTO
     * @see com.johan.ferreteria.dto.SaleResponseDTO
     * @param id identificador de la venta (Sale)
     * @return un iterable con todos los detalles de esa venta
     */
    Iterable<SaleDetail> findBySale_Id(Long id);

}
