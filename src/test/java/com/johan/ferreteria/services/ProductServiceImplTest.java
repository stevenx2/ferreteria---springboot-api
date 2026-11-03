package com.johan.ferreteria.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.johan.ferreteria.dto.ProductRequestDTO;
import com.johan.ferreteria.dto.ProductResponseDTO;
import com.johan.ferreteria.problem.ResourceNotFoundException;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.test.annotation.DirtiesContext;

@Sql(
        scripts = {
                "classpath:testcontainers/clean-data.sql",
                "classpath:testcontainers/data.sql"
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
@SpringBootTest
@Transactional
public class ProductServiceImplTest {

    @Autowired
    private ProductService productService;

    @Test
    void testFindAll() {
        List<ProductResponseDTO> products = productService.findAll();

        assertNotNull(products);
        assertEquals(6, products.size());
        products.forEach(System.out::println);
    }

    @Test
    void testFindByNameContaining() {
        List<ProductResponseDTO> products = productService.findByNameContaining("tornillos");

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Caja de tornillos autorroscantes x 100", products.getFirst().getName());
    }

    @Test
    void testFindByPriceBetween() {

        List<ProductResponseDTO> products = productService.findByPriceBetween(28000, 200000); // busco producto con
                                                                                              // precios de 28.000 a
                                                                                              // 200.000 incluyendo los
                                                                                              // extremos

        assertNotNull(products);
        assertEquals(4, products.size()); // solo 4 productos de los 6 cumplen con la consigna
    }

    /**
     * test para encontrar todos los productos de un proveedor
     */
    @Test
    void testFindBySupplierId() {

        List<ProductResponseDTO> products = productService.findBySupplierId(6L);

        assertNotNull(products);
        assertEquals(1, products.size()); // el proveedor solo tiene un producto
        assertEquals("Cemento gris x 50kg", products.getFirst().getName()); // el nombre del único producto del
                                                                            // proveedor

        // Lanza una excepción si el proveedor no existe
        assertThrows(ResourceNotFoundException.class, () -> productService.findBySupplierId(999l));

    }

    @Test
    void testSave() {
        // creoel producto que voy a guardar
        ProductRequestDTO request = new ProductRequestDTO("Producto ejemplo", 30000, 300, 1L);

        ProductResponseDTO saved = productService.save(request);

        // valido que los campos del producto guardado
        assertEquals("Producto ejemplo", saved.getName());
        assertEquals(30000, saved.getPrice());
        assertEquals(300, saved.getStock());

        // Encuentro los productos del proveedor con id 1. Debe tener 2 productos
        // contando este nuevo
        List<ProductResponseDTO> supplierProducts = productService.findBySupplierId(1L);
        assertEquals(2, supplierProducts.size());

        // creo un array con los 2 nombres de los productos del proveedor y luego valido
        // que en efecto el proveedor tiene esos 2 productos contando el nuevo que acabo
        // de guardar
        assertArrayEquals(
                new String[] {
                        "Juego de destornilladores x 6 pzas",
                        "Producto ejemplo"
                },
                supplierProducts.stream().map(ProductResponseDTO::getName).toArray());

        // debe haber un producto mas en la base de datos
        assertEquals(7, productService.findAll().size());

        // si el proveedor que se le quiere asignar a un producto no existe, entonces
        // lanza una excepción de tipo ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class,
                () -> productService.save(new ProductRequestDTO("", 1, 1, 9999L)));

    }

    @Test
    void testUpdate() {

        ProductResponseDTO fromDb = productService.findById(1L); // este registro inicialmente es: ('Cemento gris x
                                                                 // 50kg', 32000.00, 150, 6)

        /**
         * creo el objeto para actualizar el anterior. solo voy a cambiar su proveedor
         * del 6 al 2 y aumentar su stock en 100, que daría un total
         * de 250 en el stock en total
         */
        ProductRequestDTO request = new ProductRequestDTO(fromDb.getName(), fromDb.getPrice(), fromDb.getStock() + 100,
                2L);

        // actualizo el producto usando el id de la variable fromDb (1)
        ProductResponseDTO updated = productService.update(fromDb.getId(), request);
        assertEquals("Cemento gris x 50kg", updated.getName()); // nombre sigue igual
        assertEquals(32000, updated.getPrice()); // precio sigue igual
        assertEquals(250, updated.getStock()); // stock aumenta 100 unidades
        assertEquals(2, updated.getSupplier_id()); // su proveedor ahora es el 2

        // el viejo proveedor del producto ahora no debe tener ningún producto
        List<ProductResponseDTO> oldSupplierProducts = productService.findBySupplierId(6L);
        assertEquals(0, oldSupplierProducts.size());

        // el nuevo proveedor ahora tiene un producto mas, en este caso 2
        List<ProductResponseDTO> newSupplierProducts = productService.findBySupplierId(2l);
        assertEquals(2, newSupplierProducts.size());

        System.out.println("----------------------------------------------PRODUCTOS PROVEEDOR 2");
        newSupplierProducts.forEach(System.out::println);


    }

}
