package com.restaurante.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restaurante.model.Cardapio;

public interface CardapioRepository extends JpaRepository<Cardapio, Long> {
}

