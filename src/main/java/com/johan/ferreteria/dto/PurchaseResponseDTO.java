package com.johan.ferreteria.dto;


import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO de respuestas en la api para la entidad Purchase
 * @see  com.johan.ferreteria.entities.Purchase
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class PurchaseResponseDTO {

    private Long id;

    private LocalDateTime date;

    private Long supplierId;
    
    private float totalCost;
}
