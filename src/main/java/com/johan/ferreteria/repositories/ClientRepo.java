package com.johan.ferreteria.repositories;

import com.johan.ferreteria.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClientRepo extends JpaRepository<Client, Long> {
}
