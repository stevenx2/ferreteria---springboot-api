package com.johan.ferreteria.services;

import com.johan.ferreteria.dto.ClientResponseDTO;
import com.johan.ferreteria.dto.SaleRequestDTO;
import com.johan.ferreteria.dto.SaleResponseDTO;
import com.johan.ferreteria.problem.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.test.annotation.DirtiesContext;

/**
 * tests para el servicio de ventas
 */
@Sql(
        scripts = {
                "classpath:testcontainers/clean-data.sql",
                "classpath:testcontainers/data.sql"
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
@Transactional
@SpringBootTest
public class SaleServiceImplTest {


    @Autowired
    private SaleService saleService;

    @Autowired
    private ClientService clientService;





    @Test
    void testFindAll(){

        List<SaleResponseDTO> sales = saleService.findAll();
        assertNotNull(sales);
        assertEquals(6,sales.size());
        sales.forEach(System.out::println);
    }



    @Test
    void testFindByClientId(){
        //Ventas al cliente con id 1
        List<SaleResponseDTO> sales = saleService.findByClientId(1L);

        assertNotNull(sales);
        assertEquals(1,sales.size()); // solo existe una venta a ese cliente
        assertEquals(1130000,sales.getFirst().getTotalPrice()); // el total dela venta es 1.130.000
        assertEquals(2026,sales.getFirst().getDate().getYear());
        assertEquals(Month.JUNE,sales.getFirst().getDate().getMonth());
        assertEquals(1,sales.getFirst().getDate().getDayOfMonth());
    }


    @Test
    void testFindByClientName(){
        List<SaleResponseDTO> sales = saleService.findByClientName("Carlos Pérez");

        assertNotNull(sales);
        assertEquals(1,sales.size()); // solo existe una venta a ese cliente
        assertEquals(2026,sales.getFirst().getDate().getYear());
        assertEquals(Month.JUNE,sales.getFirst().getDate().getMonth());
        assertEquals(1,sales.getFirst().getDate().getDayOfMonth());
    }


    @Test
    void testSave(){
        //creo la venta a guardar. Solo basta con el id del cliente, ya que la fecha se genera automáticamente
        SaleRequestDTO request = new SaleRequestDTO(2L);

        //guardo la venta
        SaleResponseDTO saved = saleService.save(request);

        assertEquals(2L,saved.getClientId());
        assertEquals(7,saleService.findAll().size()); // debe existir un registro mas en la base de datos

        //Valido que el cliente se asignó a la venta correctamente
        ClientResponseDTO client = clientService.findById(saved.getClientId());
        assertEquals("María Gómez",client.getName());
        assertEquals("+573149871234",client.getPhone_number());
        assertEquals("Calle 50 # 30-15, Bogotá",client.getAddress());

        //debe lanzar una excepción si el cliente no existe
        assertThrows(ResourceNotFoundException.class,()-> saleService.save(new SaleRequestDTO(999L)));

        System.out.println(saved);
    }


    @Test
    void testUpdate(){
        // la venta con id 1 ahora va a apuntar al cliente con id 2 en lugar del cliente con id 1
        SaleRequestDTO request = new SaleRequestDTO(2L);
        SaleResponseDTO updatedSale = saleService.update(1L, request);

        assertEquals(2L,updatedSale.getClientId());

        //la fecha debe seguir igual, esta no se puede editar.
        assertEquals(2026,updatedSale.getDate().getYear());
        assertEquals(Month.JUNE,updatedSale.getDate().getMonth());
        assertEquals(1,updatedSale.getDate().getDayOfMonth());

        //no debe existir ventas al cliente anterior, teniendo en cuenta que se le quitó la única que tenia
        List<SaleResponseDTO> oldClientSales = saleService.findByClientId(1L);
        assertEquals(0,oldClientSales.size());

        //debe existir 2 ventas al nuevo cliente, contando la nueva que se le asignó
        List<SaleResponseDTO> newClientSales = saleService.findByClientId(2L);
        assertEquals(2,newClientSales.size());

        //Si se quiere actualizar una venta que no existe, lanza excepción
        assertThrows(ResourceNotFoundException.class,()-> saleService.update(999L,new SaleRequestDTO(1L)));

        //Si se le quiere asignar un cliente que no existe a una venta, lanza excepción
        assertThrows(ResourceNotFoundException.class,()-> saleService.update(1L,new SaleRequestDTO(9999L)));

    }

}
