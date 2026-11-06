package com.johan.ferreteria.services;


import com.johan.ferreteria.dto.SaleDetailRequestDTO;
import com.johan.ferreteria.dto.SaleDetailResponseDTO;
import com.johan.ferreteria.problem.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Sql(
        scripts = {
                "classpath:testcontainers/clean-data.sql",
                "classpath:testcontainers/data.sql"
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
@SpringBootTest
@Transactional
public class SaleDetailServiceImplTest {


    @Autowired
    private SaleDetailService saleDetailService;



    @Test
    void testFindAll(){
        List<SaleDetailResponseDTO> details = saleDetailService.findAll();

        assertNotNull(details);
        assertEquals(9,details.size());
        details.forEach(System.out::println);

    }


    // Test para buscar todos los detalles de venta entre un rango de precios
    @Test
    void testFindAllByUnitPriceBetween(){
        //busco detalles con valores de 100.000 a 200.000
        List<SaleDetailResponseDTO> details = saleDetailService.findByUnitPriceBetween(100000, 200000);

        assertNotNull(details);
        assertEquals(3,details.size());

    }


    //Test para encontrar todos los detalles de venta con el identificador de la venta
    @Test
    void testFindBySaleId(){

        List<SaleDetailResponseDTO> details = saleDetailService.findBySaleId(1L);

        assertNotNull(details);
        assertEquals(4,details.size());  // la venta debe tener 4 detalles



        double totalPrice = details
                .stream()
                .mapToDouble(d -> d.getQuantity() * d.getUnit_price())
                .sum();

        //el total de la venta con identificador 1 debe ser 1.130.000
        assertEquals(1130000,totalPrice);

    }




    @Test
    void testSave(){

        SaleDetailRequestDTO request = new SaleDetailRequestDTO(1L,1L,2);

        SaleDetailResponseDTO response = saleDetailService.save(request);
        assertEquals(1,request.getProductId());
        assertEquals(1,request.getSaleId());
        assertEquals(2,response.getQuantity());
        // el precio unitario debe ser el mismo precio actual del producto, en este caso el producto con id 1 vale 32.000
        assertEquals(32000,response.getUnit_price());



        //Los detalles de la venta con id 1 ahora deben ser 5, contando este nuevo
        List<SaleDetailResponseDTO> details = saleDetailService.findBySaleId(1L);
        assertEquals(5,details.size());


        double totalPrice = details
                .stream()
                .mapToDouble(d -> d.getQuantity() * d.getUnit_price())
                .sum();

        /**
         * esta venta anteriormente tenia un precio de 1.130.000, pero si le sumamos este nuevo detalle (2 unitades de un producto que su unidad vale 32000)
         * sería igual a 1130000 + (32000 + 2) = 1.194.000
         */
        assertEquals(1194000,totalPrice);


        //Lanza excepción si la venta que se le quiere asignar al detalle no existe
        assertThrows(ResourceNotFoundException.class,() -> saleDetailService.save(new SaleDetailRequestDTO(999L,1L,2)));

        //Lanza excepción si el producto que se le quiere asignar al detalle no existe
        assertThrows(ResourceNotFoundException.class,() -> saleDetailService.save(new SaleDetailRequestDTO(1L,99999L,2)));
    }



    @Test
    void testFindById(){
        SaleDetailResponseDTO detail = saleDetailService.findById(1L);
        assertEquals(1,detail.getProductId());
        assertEquals(1,detail.getSaleId());
        assertEquals(5,detail.getQuantity());
        assertEquals(32000,detail.getUnit_price());

        //Lanza excepción si no encuentra el detalle
        assertThrows(ResourceNotFoundException.class,()-> saleDetailService.findById(999L));
    }




}
