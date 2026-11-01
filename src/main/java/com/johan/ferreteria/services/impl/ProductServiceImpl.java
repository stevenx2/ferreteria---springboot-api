package com.johan.ferreteria.services.impl;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.johan.ferreteria.dto.ProductRequestDTO;
import com.johan.ferreteria.dto.ProductResponseDTO;
import com.johan.ferreteria.entities.Product;
import com.johan.ferreteria.entities.Supplier;
import com.johan.ferreteria.repositories.ProductRepo;
import com.johan.ferreteria.repositories.SupplierRepo;
import com.johan.ferreteria.services.ProductService;
import com.johan.problem.ResourceNotFoundException;

/**
 * implemantación del servicio de productos
 * ProductServiceImpl
 */
@Transactional
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final MessageSource messageSource;
    private final SupplierRepo supplierRepo;

    public ProductServiceImpl(ProductRepo productRepo, MessageSource messageSource, SupplierRepo supplierRepo) {
        this.productRepo = productRepo;
        this.messageSource = messageSource;
        this.supplierRepo = supplierRepo;
    }

    /**
     * elimina un proveedor por su id, si el id no pertenece a ningún proveedor
     * lanza una excepción de tipo ResourceNotFound
     * 
     * @see package {@link com.johan.problem.ResourceNotFoundException}
     */
    @Override
    public void deleteById(Long id) {

        if (!productRepo.existsById(id)) {
            throw new ResourceNotFoundException(
                    messageSource.getMessage("resource.notfound", new Object[] { id },
                            LocaleContextHolder.getLocale()));

        }

        productRepo.deleteById(id);

    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductResponseDTO> findAll() {
        return productRepo.findAll()
                .stream()
                .map(this::mapEntityToResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductResponseDTO> findAllByPage(Pageable pageable) {
        return productRepo.findAll(pageable)
                .map(this::mapEntityToResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public ProductResponseDTO findById(Long id) {

        Product found = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("resource.notfound", new Object[] { id },
                                LocaleContextHolder.getLocale())));

        return this.mapEntityToResponseDto(found);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductResponseDTO> findByNameContaining(String name) {
        return StreamSupport.stream(
                productRepo.findByNameContaining(name).spliterator(), false)
                .map(this::mapEntityToResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductResponseDTO> findByPriceBetween(double minPrice, double max_price) {
        return StreamSupport.stream(
                productRepo.findByPriceBetween(minPrice, max_price).spliterator(), false)
                .map(this::mapEntityToResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductResponseDTO> findByPriceGreaterThan(double price) {
        return StreamSupport.stream(
                productRepo.findByPriceGreaterThan(price).spliterator(), false)
                .map(this::mapEntityToResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductResponseDTO> findByPriceLessThan(double price) {
        return StreamSupport.stream(
                productRepo.findByPriceLessThan(price).spliterator(), false)
                .map(this::mapEntityToResponseDto)
                .toList();
    }

    /**
     * 
     * Obtener todos los productos de un cierto proveedor. Este método existe porque
     * el DTO de supplier no expone los productos.
     * Además esté método devuelve un lista de los productos pero de la clase DTO de
     * respuestas (ProductResponseDTO)
     * 
     * @see com.johan.ferreteria.dto.ProductResponseDTO
     */
    @Transactional(readOnly = true)
    @Override
    public List<ProductResponseDTO> findBySupplierId(Long id) {
        // Obtengo el proveedor o lanza excepción si no existe
        Supplier supplier = supplierRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("resource.notfound", new Object[] { id },
                                LocaleContextHolder.getLocale())));

        // Obtengo los productos del proveedor que encontré y los mapeo a la clase DTO
        // mediante el método mapEntityToResponseDto de la interfaz que implementa este
        // servicio
        return supplier.getProducts().stream().map(this::mapEntityToResponseDto).toList();
    }

    @Override
    public ProductResponseDTO save(ProductRequestDTO product) {
        // Obtengo el proveedor o lanza excepción si no existe
        Supplier supplier = supplierRepo.findById(product.getSupplier_id())
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("product.supplierId.notFound",
                                new Object[] { product.getSupplier_id() },
                                LocaleContextHolder.getLocale())));

        // creo el producto que voy a guardar
        Product newProduct = new Product();
        newProduct.setName(product.getName());
        newProduct.setPrice(product.getPrice());
        newProduct.setStock(product.getStock());
        newProduct.setSupplier(supplier); // le asigno el proveedor que encontré mediante el id

        Product saved = productRepo.save(newProduct);

        return this.mapEntityToResponseDto(saved);
    }

    @Override
    public ProductResponseDTO update(Long id, ProductRequestDTO product) {
        // Obtengo el producto que quiero actualizar
        Product fromDb = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("resource.notfound", new Object[] { id },
                                LocaleContextHolder.getLocale())));

        // Obtengo el proveedor o lanza excepción si no existe. El campo supplier_id es
        // obligatorio en el DTO, por lo que se debe buscar al proveedor aunque no se
        // haya actualizado
        Supplier newSupplier = supplierRepo.findById(product.getSupplier_id())
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("product.supplierId.notFound",
                                new Object[] { product.getSupplier_id() },
                                LocaleContextHolder.getLocale())));

        /**
         * solo le asigne el nuevo proveedor si no es el mismo que tenia anteriormente.
         * el campo del id del proveedor no puede ser nulo en ProductRequestDTO,
         * por lo que esta verificación es imprescindible
         * 
         */
        Supplier oldSupplier = fromDb.getSupplier();
        if (!newSupplier.equals(oldSupplier)) {
            // elimino al producto del anterior proveedor y lo agrego al nuevo
            oldSupplier.removeProduct(fromDb);
            newSupplier.addProduct(fromDb);
        }

        fromDb.setName(product.getName());
        fromDb.setPrice(product.getPrice());
        fromDb.setStock(product.getStock());

        // actualizo al producto
        Product saved = productRepo.save(fromDb);

        // lo mapeo al DTO y retorno
        return this.mapEntityToResponseDto(saved);
    }

}
