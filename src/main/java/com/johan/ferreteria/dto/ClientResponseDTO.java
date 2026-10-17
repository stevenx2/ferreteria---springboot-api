package com.johan.ferreteria.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 
 * ClientResponseDTO
 * Objeto DTO para las respuestas en la entidad Cliente.
 */
@Setter 
@Getter
@ToString 
public class ClientResponseDTO {

    /**
     * a diferencia de la clase de peticiones, en esta si expongo el id.
     */
    private Long client_id;

  
    private String name;

    private String phone_number;

    private String address;

    
    
}
