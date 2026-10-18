package com.johan.ferreteria.services.impl;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.johan.ferreteria.dto.ClientRequestDTO;
import com.johan.ferreteria.dto.ClientResponseDTO;
import com.johan.ferreteria.entities.Client;
import com.johan.ferreteria.repositories.ClientRepo;
import com.johan.ferreteria.services.ClientService;
import com.johan.problem.ResourceNotFoundException;

/**
 * 
 * ClientServiceImpl
 * Servicio de los clientes
 */
@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepo clientRepo;
    private final MessageSource messageSource;

    public ClientServiceImpl(ClientRepo clientRepo, MessageSource messageSource) {
        this.clientRepo = clientRepo;
        this.messageSource = messageSource;
    }

    @Override
    public void deleteById(Long id) {

        if (!clientRepo.existsById(id)) {
            throw new ResourceNotFoundException(
                    messageSource.getMessage("resource.notfound", new Object[] { id },
                            LocaleContextHolder.getLocale()));

        }

        clientRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ClientResponseDTO> findAll() {
        return clientRepo.findAll()
                .stream()
                .map(this::mapEntityToResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
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

        // guarda al cliente
        Client saved = clientRepo.save(toSave);

        /**
         * mapeo el Client a ClientResponseDto usando el método de la interfaz
         * ClientService (la que está implementando esta clase)
         */
        return mapEntityToResponseDto(saved);
    }

    @Override
    public ClientResponseDTO update(Long id, ClientRequestDTO client) {

        Client found = clientRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(messageSource.getMessage("resource.notfound", new Object[] { id },
                        LocaleContextHolder.getLocale())));

        found.setAddress(client.getAddress());
        found.setName(client.getName());
        found.setPhone_number(client.getPhone_number());

        Client saved = clientRepo.save(found);
        return mapEntityToResponseDto(saved);
    }

    @Override
    public ClientResponseDTO findById(Long id) {

        Client found = clientRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(messageSource.getMessage("resource.notfound", new Object[] { id },
                        LocaleContextHolder.getLocale())));

        return mapEntityToResponseDto(found);
    }

    @Transactional (readOnly = true)
    @Override
    public List<ClientResponseDTO> findByNameContaining(String name) {
        return StreamSupport
                .stream(clientRepo.findByNameContaining(name).spliterator(), false)
                .map(this::mapEntityToResponseDto)
                .toList();

    }

}
