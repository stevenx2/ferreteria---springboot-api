package com.johan.ferreteria.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * PurchaseDetail
 * entidad que representa a cada detalle individual de un producto y su cantidad
 * comprada por parte de la
 * ferreteria a algún proveedor
 */
@Setter
@Getter
@Entity
@Table(name = "purchase_details")
public class PurchaseDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    @ManyToOne
    @JoinColumn(name = "product_id ")
    private Product product;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "unit_cost")
    private int unit_cost;

}
