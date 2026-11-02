package com.johan.ferreteria.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * Client
 * Entidad de la base de datos que representa a los clientes que hacen compras de productos
 */

@EqualsAndHashCode 
@ToString 
@Setter 
@Getter 
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long id;


    @Column (name = "name",unique = true)
    private String name;

    @Column (name = "phone_number")
    private String phone_number;

    @Column (name = "address ")
    private String address;
}
