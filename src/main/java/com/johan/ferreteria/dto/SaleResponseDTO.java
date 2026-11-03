package com.johan.ferreteria.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * clase DTO para las respuestas de la api en la entidad SALE
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class SaleResponseDTO {

    private Long id;

    private LocalDateTime date;

    private Long clientId;

    private double totalPrice;


}
