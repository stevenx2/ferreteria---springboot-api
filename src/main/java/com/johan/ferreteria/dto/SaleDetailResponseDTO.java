package com.johan.ferreteria.dto;

import lombok.*;

/**
 * DTO para respuestas en la api para la entidad SaleDetail (los detalles de las ventas)
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class SaleDetailResponseDTO {

    private Long id;

    private Long saleId;

    private Long productId;

    private int quantity;

    private float unit_price;
}
