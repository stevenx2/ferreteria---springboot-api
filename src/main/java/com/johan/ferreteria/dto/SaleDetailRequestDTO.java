package com.johan.ferreteria.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de peticiones en la api para la entidad SaleDetail
 * @see com.johan.ferreteria.entities.SaleDetail
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SaleDetailRequestDTO {

    @NotNull(message = "{saleDetail.saleId.notNull}")
    private Long saleId;

    @NotNull(message = "{saleDetail.productId.notNull}")
    private Long productId;

    @Positive(message = "{saleDetail.quantity.positive}")
    private int quantity;

}
