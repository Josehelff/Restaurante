package com.restaurante.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.restaurante.model.Login;

public interface LoginRepository extends JpaRepository<Login, Long> {
    Optional<Login> findByUsername(String username);
}
