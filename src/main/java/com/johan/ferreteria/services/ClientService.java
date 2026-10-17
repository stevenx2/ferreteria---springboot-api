package com.johan.ferreteria.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.johan.ferreteria.dto.ClientRequestDTO;
import com.johan.ferreteria.dto.ClientResponseDTO;
import com.johan.ferreteria.entities.Client;

public interface ClientService {



    /**
     * @param client
     * @return
     * Método para usarse en las implementaciones del servicio. Mapea el objeto que viene de la base de datos a un DTO de respuesta.
     * Actualmente el DTO tiene los mismos campos que la clase entidad, pero lo hago de esta manera por si quiero exponer menos datos en 
     * la la clase ClientResponseDTO
     */
    default ClientResponseDTO mapEntityToResponseDto(Client client) {
        ClientResponseDTO response = new ClientResponseDTO();
        response.setClient_id(client.getClient_id());
        response.setAddress(client.getAddress());
        response.setName(client.getName());
        response.setPhone_number(client.getPhone_number());
        return response;
    }


    

    List<ClientResponseDTO> findAll();

    Page<ClientResponseDTO> findAllByPage(Pageable pageable);

    ClientResponseDTO save(ClientRequestDTO client);

    ClientResponseDTO update(Long id, ClientRequestDTO client);

    void deleteById(Long id);
}
