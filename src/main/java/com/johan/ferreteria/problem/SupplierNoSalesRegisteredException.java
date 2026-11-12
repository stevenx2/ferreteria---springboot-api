package com.johan.ferreteria.problem;

/**
 * Excepción personalizada cuando se quiera buscar todas las compras que se le hizo a un
 * proveedor pero este no tenga ninguna
 */
public class SupplierNoSalesRegisteredException extends RuntimeException {
    public SupplierNoSalesRegisteredException(String message) {
        super(message);
    }
}
