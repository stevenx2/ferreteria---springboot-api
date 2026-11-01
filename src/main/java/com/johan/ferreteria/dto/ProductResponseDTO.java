package com.johan.ferreteria.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * ProductResponseDTO
 * DTO para las respuestas de la entidad de los productos
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ProductResponseDTO {

    private Long id;

    private String name;

    private double price;

    private int stock;

    private Long supplier_id;
}
