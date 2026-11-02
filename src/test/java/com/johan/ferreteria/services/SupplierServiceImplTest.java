package com.johan.ferreteria.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.johan.ferreteria.dto.SupplierRequestDTO;
import com.johan.ferreteria.dto.SupplierResponseDTO;
import com.johan.ferreteria.problem.ResourceNotFoundException;

@SpringBootTest
@Transactional
public class SupplierServiceImplTest {

    @Autowired
    private SupplierService supplierService;

    @Test
    void testFindAll() {
        List<SupplierResponseDTO> suppliers = supplierService.findAll();
        assertEquals(6, suppliers.size());
        suppliers.forEach(System.out::println);
    }

    @Test
    void testFindAllByPage() {
        PageRequest pageRequest = PageRequest.of(0, 3);
        Page<SupplierResponseDTO> result = supplierService.findAllByPage(pageRequest);

        List<SupplierResponseDTO> suppliers = result.getContent();
        assertEquals(3, suppliers.size());

        // Esta deber ser la primera página de contenido y además debe existir otra
        // después de esta, ya que hay 6 suppliers en total
        assertTrue(result.isFirst());
        assertTrue(result.hasNext());

    }

    /**
     * Test para buscar un proveedor que tenga un nombre que contenga la cadena de
     * texto dada. En este caso al buscar
     * 'Construcción' encuentra el proveedor 'Herramientas y Construcción Andina'.
     */
    @Test
    void testFindByNameContaining() {

        List<SupplierResponseDTO> byNameContaining = supplierService.findByNameContaining("Construcción");

        // Solo un proveedor tiene esa palabra en su nombre
        assertEquals(1, byNameContaining.size());
        // Valido su nombre completo
        assertEquals("Herramientas y Construcción Andina", byNameContaining.getFirst().getName());
    }

    /**
     * Test para el método de guardar un proveedor. Valido que tanto el DTO de
     * petición (SupplierRequestDTO) como el de
     * respuesta (SupplierResponseDTO) se emplean de manera correcta para evitar
     * usar el objeto original de la entidad de la
     * base de datos
     */
    @Test
    void testSave() {
        SupplierRequestDTO request = new SupplierRequestDTO("Proveedor de prueba", "3235446785",
                "La candelaria, Valle del cauca");

        SupplierResponseDTO saved = supplierService.save(request);

        // Debe existir un registro mas en los proveedores
        assertEquals(7, supplierService.findAll().size());

        // Valido los campos del objeto devuelto por el método save
        assertEquals("Proveedor de prueba", saved.getName());
        assertEquals("La candelaria, Valle del cauca", saved.getAddress());
        assertEquals("3235446785", saved.getPhone_number());

    }


    @Test 
    void testFindById() {
        SupplierResponseDTO found = supplierService.findById(1L);

        assertEquals("Ferretería Mayorista El Ancla S.A.", found.getName());
        assertEquals("+573104567890", found.getPhone_number());
        assertEquals("Calle 10 # 45-20, Bogotá", found.getAddress());

        //Valido que lanza excepción cuando el recurso no existe
        assertThrows(ResourceNotFoundException.class, () -> supplierService.findById(999L));
    }
    

    /**
     * Test para actualizar un proveedor
     */
    @Test 
    void testUpdate() {
        //Primero busco al proveedor que quiero actualizar. En este caso el que tiene el id 1
        SupplierResponseDTO found = supplierService.findById(1L);

        // el nuevo nombre deberia ser 'Ferretería Mayorista El Ancla S.A. test'
        String newName = found.getName().concat(" test");
        //Conservo todos los campos iguales, excepto por el nombre, al cual le concateno la cadena " test" 
        SupplierRequestDTO request = new SupplierRequestDTO(newName, found.getPhone_number(), found.getAddress());

        //actualizo al proveedor
        SupplierResponseDTO updated = supplierService.update(found.getSupplier_id(), request);

        //valido que sus compos siguen igual exceptuando el nombre
        assertEquals("+573104567890", updated.getPhone_number());
        assertEquals("Calle 10 # 45-20, Bogotá", updated.getAddress());
        assertEquals("Ferretería Mayorista El Ancla S.A. test", updated.getName());

    }

}
