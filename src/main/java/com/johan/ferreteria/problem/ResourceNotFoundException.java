package com.johan.ferreteria.problem;

/**
 * 
 * ResourceNotFoundException
 * Exepción lanzada cuando no existe un recurso que se está intentando buscar, ya sea con un id u otra propiedad
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    
}
