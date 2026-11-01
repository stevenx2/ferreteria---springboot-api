package com.johan.ferreteria.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * ProductRequestDTO
 * DTO para las peticiones de la entidad de productos.
 */

@NoArgsConstructor 
@AllArgsConstructor 
@Getter
@Setter 
public class ProductRequestDTO {


    @NotBlank(message = "{product.name.notBlank}")
    @Size(max = 50, message = "{product.name.size}")
    private String name;

    @NotNull(message = "{product.price.notNull}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{product.price.min}")
    private double price;

    @NotNull(message = "{product.stock.notNull}")
    @Min(value = 0, message = "{product.stock.min}")
    private int stock;

    @NotNull(message = "{product.supplierId.notNull}")
    private Long supplier_id;

}
