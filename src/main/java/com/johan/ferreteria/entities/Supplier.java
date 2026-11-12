package com.johan.ferreteria.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * Supplier
 * entidad que representan a los proveedores de productos 
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "suppliers")
@Getter
@Setter
public class Supplier {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Long id;

    @EqualsAndHashCode.Include
    @Column(name = "name",unique = true)
    private String name;

    @EqualsAndHashCode.Include
    @Column(name = "phone_number")
    private String phone_number;

    @EqualsAndHashCode.Include
    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "supplier", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    public boolean addProduct(Product newProduct) {
        newProduct.setSupplier(this);
        return products.add(newProduct);
    }

    public void removeProduct(Product product) {
        product.setSupplier(null);
        products.remove(product);
    }

}
