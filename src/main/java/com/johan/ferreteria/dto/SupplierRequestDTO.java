package com.johan.ferreteria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor 
@AllArgsConstructor 
@Getter
@Setter 
public class SupplierRequestDTO {

    @NotBlank(message = "{supplier.name.notBlank}")
    @Size(max = 20, message = "{supplier.name.size}")
    private String name;

    @NotBlank(message = "{supplier.phoneNumber.notBlank}")
    @Pattern(regexp = "^\\d{10}$", message = "{supplier.phoneNumber.pattern}")
    private String phone_number;

    @Size(max = 200, message = "{supplier.address.size}")
    private String address;

}
