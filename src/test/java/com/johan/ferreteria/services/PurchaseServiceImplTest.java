package com.johan.ferreteria.services;

import com.johan.ferreteria.dto.PurchaseRequestDTO;
import com.johan.ferreteria.dto.PurchaseResponseDTO;
import com.johan.ferreteria.dto.SupplierRequestDTO;
import com.johan.ferreteria.problem.ResourceNotFoundException;
import com.johan.ferreteria.problem.SupplierNoSalesRegisteredException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.time.Month;
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
public class PurchaseServiceImplTest {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private SupplierService supplierService;



    @Test
    void testFindAll(){
        List<PurchaseResponseDTO> purchases = purchaseService.findAll();

        assertNotNull(purchases);
        assertEquals(6,purchases.size());
        purchases.forEach(System.out::println);

    }


    @Test
    void testAllByPage(){
        Pageable pageable = PageRequest.of(0,3);

        Page<PurchaseResponseDTO> response = purchaseService.findAllByPage(pageable);

        List<PurchaseResponseDTO> purchases = response.getContent();
        assertTrue(response.hasContent()); // la página no debe estar vacia
        assertEquals(3,purchases.size());
        assertEquals(2,response.getTotalPages()); // deben existir 2 páginas (3 elementos por página)
        assertTrue(response.isFirst()); // esta es la primera página

    }


    /**
     * test para buscar todas las compras de la ferreteria a un proveedor mediante el nombre
     * de este
     */
    @Test
    void testFindAllBySupplierName(){

        List<PurchaseResponseDTO> purchases = purchaseService.findAllBySupplierName("Ferretería Mayorista El Ancla S.A.");

        assertNotNull(purchases);
        assertEquals(1,purchases.size()); // el proveedor debe contener 1 venta a la ferreteria
        assertEquals(1000000,purchases.getFirst().getTotalCost()); // el costo de la venta de ser de 1.000.000

    }

    /**
     * test para verificar los casos en el que el método FindAllBySupplierName() puede fallar
     */
    @Test
    void testFindAllBySupplierNameThrowsExceptions(){
        // si el nombre del proveedor no existe, debe lanzar la excepción ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class,()-> purchaseService.findAllBySupplierName("dwdwdwdw"));

        //inserto un proveedor de prueba (nombre 'Example') que no tiene ninguna venta a la ferreteria para el test de la linea de abajo
        supplierService.save(new SupplierRequestDTO("Example","3236482958","example"));

        /**
         * Busco las compras a la ferreteria previamente insertada. Debe lanzar una excepción de tipo
         * SupplierNoSalesRegisteredException porque no tiene ninguna venta a la ferreteria
         */
        assertThrows(SupplierNoSalesRegisteredException.class,()-> purchaseService.findAllBySupplierName("Example"));

    }


    /**
     * test cuando buscar las compras a un proveedor sale correctamente
     */
    @Test
    void testFindAllBySupplierId(){
        List<PurchaseResponseDTO> purchases = purchaseService.findAllBySupplierId(2L);
        assertEquals(1,purchases.size()); // debe existir una sola compra

        PurchaseResponseDTO first = purchases.getFirst();
        //valido la fecha
        assertEquals(2026,first.getDate().getYear());
        assertEquals(Month.MAY,first.getDate().getMonth());
        assertEquals(12,first.getDate().getDayOfMonth());

        //el costo total debe ser 1.400.000 ya que se compró 10 productos de 14.000 cada uno
        assertEquals(1400000,first.getTotalCost());

    }

    /**
     * test que contempla los errores que pueden ocurrir en el método FindAllBySupplierId()
     */
    @Test
    void testFindAllBySupplierIdThrowsExceptions(){

        //cuando el proveedor no existe, lanza excepción de tipo ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class,()->purchaseService.findAllBySupplierId(9999L));

        //inserto un proveedor de prueba (id = 7) que no tiene ninguna venta a la ferreteria para el test de la linea de abajo
        supplierService.save(new SupplierRequestDTO("Example","3236482958","example"));

        /**
         * busco el proveedor recientemente insertado. Como no tiene compras, lanza excepción de tipo
         * SupplierNoSalesRegisteredException
         */
        assertThrows(SupplierNoSalesRegisteredException.class,()-> purchaseService.findAllBySupplierId(7L));
    }


    /**
     * test de buscar compra por identificador
     */
    @Test
    void testFindById(){

        PurchaseResponseDTO purchase = purchaseService.findById(2L);

        //valido la fecha
        assertEquals(2026,purchase.getDate().getYear());
        assertEquals(Month.MAY,purchase.getDate().getMonth());
        assertEquals(12,purchase.getDate().getDayOfMonth());

        //el costo total debe ser 1.400.000 ya que se compró 10 productos de 14.000 cada uno
        assertEquals(1400000,purchase.getTotalCost());

        //Lanza excepción si la compra no existe
        assertThrows(ResourceNotFoundException.class, ()-> purchaseService.findById(9999L));

    }


    /**
     * test al guardar una nueva compra
     */
    @Test
    void testSave(){
        //Una nueva compra al proveedor con identificador 1
        PurchaseRequestDTO request = new PurchaseRequestDTO(1L);

        //guardar
        PurchaseResponseDTO saved = purchaseService.save(request);

        //La fecha debe ser la actual
        LocalDateTime date = saved.getDate();
        assertEquals(LocalDateTime.now().getYear(),date.getYear());
        assertEquals(LocalDateTime.now().getMonth(),date.getMonth());
        assertEquals(LocalDateTime.now().getDayOfMonth(),date.getDayOfMonth());

        assertEquals(7,purchaseService.findAll().size()); // ahora hay un registro mas

        //Lanza excepción cuado se quiere guardar una nueva compra a un identificador que no existe
        assertThrows(ResourceNotFoundException.class,()-> purchaseService.save(new PurchaseRequestDTO(999L)));

    }


    @Test
    void testUpdate(){
        // La compra 1 pasa a ser del proveedor 1 al proveedor 2
        PurchaseRequestDTO request = new PurchaseRequestDTO(2L);
        PurchaseResponseDTO updated = purchaseService.update(1L, request);
        assertEquals(2,updated.getSupplierId());


        //el viejo proveedor ya no tiene compras.
        assertThrows(SupplierNoSalesRegisteredException.class,()-> purchaseService.findAllBySupplierId(1L));

        // eñ nuevo proveedor debe tener una compra mas, en este caso 2
        List<PurchaseResponseDTO> newSupplierPurchases = purchaseService.findAllBySupplierId(2L);
        assertEquals(2,newSupplierPurchases.size());


    }



}
