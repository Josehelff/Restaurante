package com.restaurante.Controllers;

import com.restaurante.Service.JWTService;
import com.restaurante.Service.LoginService;
import com.restaurante.model.Login;
import com.restaurante.model.Role;
import com.restaurante.repository.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controlador para gerenciar operações de autenticação e registro de usuários.
 */
@RestController
@RequestMapping("/auth")
public class LoginController {

    /**
     * Serviço de login injetado pelo Spring.
     * Usado para encontrar e salvar logins de usuários.
     */
    @Autowired
    private LoginService loginService;

    /**
     * Serviço JWT injetado pelo Spring.
     * Usado para gerar tokens JWT para autenticação.
     */
    @Autowired
    private JWTService jwtService;

    /**
     * Codificador de senha BCrypt injetado pelo Spring.
     * Usado para comparar senhas criptografadas.
     */
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Endpoint para autenticar um usuário e gerar um token JWT.
     * @param login O objeto Login contendo o nome de usuário e senha fornecidos.
     * @return Um token JWT se as credenciais forem válidas; caso contrário, uma mensagem de erro.
     */
    @PostMapping("/login")
    public String login(@RequestBody Login login) {
        
        // Encontra um login existente com o nome de usuário fornecido.
        Optional<Login> existingLogin = loginService.findByUsername(login.getUsername());

        // Verifica se o login existe e se a senha fornecida corresponde à senha armazenada.
        if (existingLogin.isPresent() && passwordEncoder.matches(login.getPassword(), existingLogin.get().getPassword())) {
            // Gera e retorna um token JWT para o usuário autenticado.
            return jwtService.generateToken(existingLogin.get());
        }
        // Retorna uma mensagem de erro se as credenciais forem inválidas.
        return "Invalid credentials"; 
    }

    /**
     * Endpoint para registrar um novo usuário.
     * @param login O objeto Login contendo as informações do novo usuário. 
     * @return O objeto Login salvo com a role associada.
     */
    @PostMapping("/register")
    public Login register(@RequestBody Login login) {
        // Encontra a role "CLIENTE" e lança uma exceção se não for encontrada.
        Role role = RoleRepository.findByNome("CLIENTE").orElseThrow(() -> new RuntimeException("Role CLIENTE not found"));

        // Associa a role CLIENTE ao novo usuário.
        login.getUser().isInRole((org.apache.catalina.Role) role);

        // Salva o novo login no banco de dados e retorna o objeto salvo.
        return loginService.saveLogin(login);
    }
}
