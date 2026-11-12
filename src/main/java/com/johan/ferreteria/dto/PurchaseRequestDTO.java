package com.johan.ferreteria.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO de peticiones en la api para la entidad Purchase
 * @see  com.johan.ferreteria.entities.Purchase
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseRequestDTO {

    @NotNull(message = "{purchase.supplierId.notNull}")
    private Long supplierId;
}
