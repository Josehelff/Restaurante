package com.restaurante.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

import com.restaurante.model.Login;
import com.restaurante.repository.LoginRepository;

@Service
public class LoginService {

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Método para salvar o login com a senha criptografada
    public Login saveLogin(Login login) {
        login.setPassword(passwordEncoder.encode(login.getPassword()));
        return loginRepository.save(login);
    }
 
    // Método para encontrar um usuário por nome de usuário
    public Optional<Login> findByUsername(String username) {
        return loginRepository.findByUsername(username);
    }
}
