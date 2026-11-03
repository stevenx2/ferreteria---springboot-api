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
import lombok.ToString;

/**
 * 
 * SaleDetail
 * entidad que representa a un detalle individual de la venta de un producto de la ferreteria a algún cliente
 */
@Setter 
@Getter 
@Entity
@Table (name = "sale_details")
@ToString
public class SaleDetail {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "detail_id")
    private Long id;

    @ManyToOne
    @JoinColumn (name = "sale_id")
    private Sale sale;

    @ManyToOne
    @JoinColumn (name = "product_id")
    private Product product;

    @Column (name = "quantity")
    private int quantity;

    @Column (name = "unit_price")
    private float unit_price;


}
