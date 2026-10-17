package com.johan.ferreteria.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

import com.johan.ferreteria.dto.ClientRequestDTO;
import com.johan.ferreteria.dto.ClientResponseDTO;

import jakarta.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest
@Testcontainers
@Transactional
public class ClientServiceTests {

    @Container
    @ServiceConnection
    static MySQLContainer mysql = new MySQLContainer("mysql:8.0").withInitScript("testcontainers/data.sql");

    @Autowired
    private ClientService clientService;








    @Test
    void testFindAll() {

        List<ClientResponseDTO> all = clientService.findAll();

        assertTrue(mysql.isRunning());
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
    
    

}