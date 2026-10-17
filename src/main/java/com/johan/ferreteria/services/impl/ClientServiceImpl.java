package com.johan.ferreteria.services.impl;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.johan.ferreteria.dto.ClientRequestDTO;
import com.johan.ferreteria.dto.ClientResponseDTO;
import com.johan.ferreteria.entities.Client;
import com.johan.ferreteria.repositories.ClientRepo;
import com.johan.ferreteria.services.ClientService;


@Service
@Transactional 
public class ClientServiceImpl implements ClientService {

    private final ClientRepo clientRepo;

    public ClientServiceImpl(ClientRepo clientRepo) {
        this.clientRepo = clientRepo;
    }

    @Override
    public void deleteById(Long id) {
      
    }


    @Transactional(readOnly = true)
    @Override
    public List<ClientResponseDTO> findAll() {
        return clientRepo.findAll()
                .stream()
                .map(this::mapEntityToResponseDto)
                .toList();
    }

    @Transactional (readOnly = true)
    @Override
    public Page<ClientResponseDTO> findAllByPage(Pageable pageable) {
        return clientRepo.findAll(pageable)
        .map(this::mapEntityToResponseDto);
    }

    @Override
    public ClientResponseDTO save(ClientRequestDTO client) {

        /**
         * Creo el cliente con los datos del DTO
         */
        Client toSave = new Client();
        toSave.setName(client.getName());
        toSave.setPhone_number(client.getPhone_number());
        toSave.setAddress(client.getAddress());


        //guarda al cliente
        Client saved = clientRepo.save(toSave);


        /**
         * mapeo el Client a ClientResponseDto usando el método de la interfaz ClientService (la que está implementando esta clase)
         */
        return Stream.of(saved).map(this::mapEntityToResponseDto).findFirst().get();
    }

    @Override
    public ClientResponseDTO update(Long id, ClientRequestDTO client) {
        return null;
    }

}
