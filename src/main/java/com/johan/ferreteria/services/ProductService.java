package com.johan.ferreteria.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.johan.ferreteria.dto.ProductRequestDTO;
import com.johan.ferreteria.dto.ProductResponseDTO;
import com.johan.ferreteria.entities.Product;

/**
 * firma de métodos para los servicios de la entidad de productos
 * ProductService
 */
public interface ProductService {

    /**
     * Método de soporte para mapear las entidades a su clase DTO cuando se necesite
     * retornar el objeto.
     * Para evitar exponer campos que no se quiera, por si a futuro cambio la
     * estructura de los campos a exponer
     * 
     * @param productf
     * @return
     */
    default ProductResponseDTO mapEntityToResponseDto(Product product) {
        return new ProductResponseDTO(product.getId(),
                product.getName(),
                product.getPrice(), product.getStock(),
                product.getSupplier().getId());
    }

    List<ProductResponseDTO> findAll();

    Page<ProductResponseDTO> findAllByPage(Pageable pageable);

    // Buscar producto que contenga la cadena 'name' en su nombre
    List<ProductResponseDTO> findByNameContaining(String name);

    // Buscar productos por un rango de precios dado (contando extremos)
    List<ProductResponseDTO> findByPriceBetween(double minPrice, double max_price);

    List<ProductResponseDTO> findByPriceLessThan(double price);

    List<ProductResponseDTO> findByPriceGreaterThan(double price);

    /**
     * Ya se el DTO de la entidad Supplier no retorna los productos de ese
     * proveedor, este método sirve para obtener la lista de productos
     * de un proveedor mediante el id de este
     * 
     * @param id identificador del proveedor
     * @return una lista que contiene todos los productos de ese proveedor
     */
    List<ProductResponseDTO> findBySupplierId(Long id);

    ProductResponseDTO findById(Long id);

    ProductResponseDTO save(ProductRequestDTO product);

    ProductResponseDTO update(Long id, ProductRequestDTO product);

    void deleteById(Long id);

}
