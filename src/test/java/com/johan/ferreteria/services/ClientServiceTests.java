package com.johan.ferreteria.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.johan.ferreteria.dto.ClientRequestDTO;
import com.johan.ferreteria.dto.ClientResponseDTO;
import com.johan.problem.ResourceNotFoundException;

import jakarta.transaction.Transactional;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * 
 * ClientServiceTests
 * Tests del servicio de clientes
 */
@SpringBootTest
@Transactional
public class ClientServiceTests {

   

    @Autowired
    private ClientService clientService;







    @Test
    void testFindAll() {

        List<ClientResponseDTO> all = clientService.findAll();

        assertEquals(6, all.size());
        all.forEach(System.out::println);
    }

    @Test
    void testFindAllByPage() {

        Pageable pageable = PageRequest.of(0, 3);

        Page<ClientResponseDTO> result = clientService.findAllByPage(pageable);

        List<ClientResponseDTO> content = result.getContent();
        assertEquals(3, content.size());
        assertTrue(result.isFirst());

    }
    



    @Test 
    void testSave() {
        ClientRequestDTO client = new ClientRequestDTO("Pepe", "3233232323", "Buenaventura #23-45");

        ClientResponseDTO saved = clientService.save(client);

        assertEquals(7, clientService.findAll().size());
        assertEquals("Pepe", saved.getName());
        assertEquals("3233232323", saved.getPhone_number());
        assertEquals("Buenaventura #23-45", saved.getAddress());

    }



    @Test 
    void testFindById() {
        ClientResponseDTO found = clientService.findById(1L);

        assertNotNull(found);
        assertEquals("Carlos Pérez", found.getName());

        //Buscar un cliente por un id que no está asignado a ninguno lanza una excepción de tipo ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () -> clientService.findById(9999L));
    }





    @Test 
    void testUpdate() {

        //Primero valido su nombre antes de actualizarlo
        ClientResponseDTO found = clientService.findById(1l);
        assertEquals("Carlos Pérez", found.getName());

        //Creo el DTO para cambiar los datos del cliente. Solo quiero cambiar el nombre
        ClientRequestDTO requestDTO = new ClientRequestDTO("Carlos Córdoba", found.getPhone_number(),
                found.getAddress());

        //Valido que el mismo cliente (id 1) ahora tiene el nuevo nombre
        ClientResponseDTO updated = clientService.update(1L, requestDTO);
        assertEquals(1L, updated.getClient_id());
        assertEquals("Carlos Córdoba", updated.getName());

        //Lanza excepción si intento actualizar cliente cuyo id no existe en la base de datos
        assertThrows(ResourceNotFoundException.class, () -> clientService.update(999L, requestDTO));

    }
    


    @Test 
    void testFindByNameContaining() {
        List<ClientResponseDTO> byNameContaining = clientService.findByNameContaining("Luis");
        assertNotNull(byNameContaining);
        assertEquals(1, byNameContaining.size());
        assertEquals("Pedro Luis Torres", byNameContaining.getFirst().getName());
    }
    
    

}