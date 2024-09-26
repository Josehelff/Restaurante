package com.restaurante.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restaurante.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}


