package com.johan.ferreteria.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class SaleRequestDTO {

    @NotNull(message = "{sale.clientId.notNull}")
    private Long clientId;
}
