package com.johan.ferreteria.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * SupplierResponseDTO
 * Clase DTO para las respuestas para la entidad de Supplier
 */
@Getter
@Setter
@ToString 
public class SupplierResponseDTO {

    private Long supplier_id;

    private String name;

    private String phone_number;

    private String address;

}
