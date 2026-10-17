package com.johan.ferreteria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * ClientRequestDTO
 * Objeto DTO para peticiones en la entidad Cliente
 */
@Getter
@Setter
public class ClientRequestDTO {

    @NotBlank(message = "{client.name.notblank}")
    @Size(min = 2, max = 100, message = "{client.name.size}")
    private String name;

    @NotBlank(message = "{client.phone.notblank}")
    @Pattern(regexp = "^[0-9]{10}$", message = "{client.phone.pattern}")
    private String phone_number;

    @Size(max = 200, message = "{client.address.size}")
    private String address;

    public ClientRequestDTO(
            String name,
            String phone_number,
            String address) {
        this.name = name;
        this.phone_number = phone_number;
        this.address = address;
    }

    public ClientRequestDTO() {
    }

}
