package com.johan.ferreteria.services.impl;

import com.johan.ferreteria.dto.PurchaseRequestDTO;
import com.johan.ferreteria.dto.PurchaseResponseDTO;
import com.johan.ferreteria.entities.Purchase;
import com.johan.ferreteria.entities.Supplier;
import com.johan.ferreteria.problem.ResourceNotFoundException;
import com.johan.ferreteria.problem.SupplierNoSalesRegisteredException;
import com.johan.ferreteria.repositories.PurchaseDetailRepo;
import com.johan.ferreteria.repositories.PurchaseRepo;
import com.johan.ferreteria.repositories.SupplierRepo;
import com.johan.ferreteria.services.PurchaseService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Transactional
@Service
public class PurchaseServiceImpl implements PurchaseService {


    private final PurchaseRepo purchaseRepo;
    private final SupplierRepo supplierRepo;
    private final MessageSource messageSource;
    private final PurchaseDetailRepo purchaseDetailRepo;


    public PurchaseServiceImpl(PurchaseRepo purchaseRepo, SupplierRepo supplierRepo, MessageSource messageSource, PurchaseDetailRepo purchaseDetailRepo) {
        this.purchaseRepo = purchaseRepo;
        this.supplierRepo = supplierRepo;
        this.messageSource = messageSource;
        this.purchaseDetailRepo = purchaseDetailRepo;
    }

    @Transactional(readOnly = true)
    @Override
    public List<PurchaseResponseDTO> findAll() {
        return purchaseRepo.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }


    @Transactional(readOnly = true)
    @Override
    public Page<PurchaseResponseDTO> findAllByPage(Pageable pageable) {
        return purchaseRepo.findAll(pageable).map(this::mapToResponseDto);
    }

    /**
     * Buscar todas las compras de la ferreteria a un proveedor en especifico mediante su nombre
     *
     * @param supplierName nombre del proveedor del que queremos las compras
     * @return lista de tipo PurchaseResponseDTO con las compras reealizas a ese proveedor
     */
    @Transactional(readOnly = true)
    @Override
    public List<PurchaseResponseDTO> findAllBySupplierName(String supplierName) {

        //Si el nombre proporcionado no pertenece a ni ningún proveedor, lanzo una excepción
        if (!supplierRepo.existsByName(supplierName)) {
            throw new ResourceNotFoundException(
                    messageSource.getMessage(
                            "supplier.name.notFound", new Object[]{supplierName}, LocaleContextHolder.getLocale()
                    )
            );
        }

        //Si no existe una compra de la ferreteria a este proveedor, lanzo una excepción
        if (!purchaseRepo.existsBySupplier_Name(supplierName)) {
            throw new SupplierNoSalesRegisteredException(
                    messageSource.getMessage(
                            "supplier.name.salesNotFound", new Object[]{supplierName}, LocaleContextHolder.getLocale()
                    )
            );
        }

        //retorno las compras luego de mapearlas a su clase DTO
        return StreamSupport.stream(
                        purchaseRepo.findBySupplier_Name(supplierName).spliterator(),
                        false
                )
                .map(this::mapToResponseDto)
                .toList();
    }


    @Override
    public List<PurchaseResponseDTO> findAllBySupplierId(Long id) {

        if (!supplierRepo.existsById(id)) {
            throw new ResourceNotFoundException(
                    messageSource.getMessage(
                            "supplier.id.notFound", new Object[]{id}, LocaleContextHolder.getLocale()
                    )
            );
        }


        //Si no existe una compra de la ferreteria a este proveedor, lanzo una excepción
        if (!purchaseRepo.existsBySupplier_Id(id)) {
            //Obtengo el nombre del proveedor
            String supplierName = supplierRepo.findSupplierNameById(id).get(); // Obtengo directamente el nombre porque en este punto el proveedor existe debido a la sentencia if de arriba
            throw new SupplierNoSalesRegisteredException(
                    messageSource.getMessage(
                            "supplier.name.salesNotFound", new Object[]{supplierName}, LocaleContextHolder.getLocale()
                    )
            );
        }

        // retorno las compras luego de mapearlas a su clase DTO
        return StreamSupport.stream(
                        purchaseRepo.findBySupplier_Id(id).spliterator(),
                        false
                )
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    public PurchaseResponseDTO findById(Long id) {
        Purchase purchase = purchaseRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("resource.notfound", new Object[]{id}, LocaleContextHolder.getLocale())
                ));


        return this.mapToResponseDto(purchase);
    }

    @Override
    public PurchaseResponseDTO update(Long id, PurchaseRequestDTO purchase) {

        Purchase fromDB = purchaseRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("resource.notfound", new Object[]{id}, LocaleContextHolder.getLocale())
                ));


        Supplier newSupplier = supplierRepo.findById(purchase.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("purchase.supplierId.notFound", new Object[]{purchase.getSupplierId()}, LocaleContextHolder.getLocale())
                ));


        Supplier oldSupplier = fromDB.getSupplier();

        if (!newSupplier.equals(oldSupplier)) {
            fromDB.setSupplier(newSupplier);
            Purchase updated = purchaseRepo.save(fromDB);
            return this.mapToResponseDto(updated);

        }


        return this.mapToResponseDto(fromDB);
    }

    @Override
    public PurchaseResponseDTO save(PurchaseRequestDTO purchase) {

        Supplier supplier = supplierRepo.findById(purchase.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("purchase.supplierId.notFound", new Object[]{purchase.getSupplierId()}, LocaleContextHolder.getLocale())
                ));


        Purchase newPurchase = new Purchase();
        newPurchase.setSupplier(supplier);

        Purchase saved = purchaseRepo.save(newPurchase);

        return this.mapToResponseDto(saved);
    }


    /**
     * Método para mapear una instancia de Purchase proveniente de la base de datos a una instancia de la clase DTO de respuestas
     * PurchaseResponseDTO
     *
     * @param purchase la instancia Purchase que se quiere mapear
     * @return la instancia DTO mapeada
     * @see com.johan.ferreteria.entities.Purchase
     * @see com.johan.ferreteria.dto.PurchaseResponseDTO
     */
    private PurchaseResponseDTO mapToResponseDto(Purchase purchase) {

        PurchaseResponseDTO response = new PurchaseResponseDTO();
        response.setId(purchase.getId());
        response.setDate(purchase.getDate());
        response.setSupplierId(purchase.getSupplier().getId());


        //Obtengo todos los detalles de la compra y sumo los precios para obtener el costo total
        double totalCost = StreamSupport.stream(
                        purchaseDetailRepo.findByPurchase_Id(purchase.getId()).spliterator(),
                        false
                )
                .mapToDouble(p -> p.getQuantity() * p.getUnitCost())
                .sum();


        //le asigno el costo total que calculé a la instancia del objeto DTO
        response.setTotalCost((float) totalCost);

        return response;
    }
}
