package com.restaurante.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.restaurante.Service.LoginService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Serviço de login injetado pelo Spring.
     * Usado para carregar detalhes do usuário e autenticação.
     */
    @Autowired
    private LoginService loginService;

    /**
     * Filtro de autenticação JWT injetado pelo Spring.
     * Usado para validar e processar tokens JWT.
     */
    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Configura a segurança HTTP, incluindo regras de autorização e gerenciamento de sessão.
     * @param http O construtor de HttpSecurity.
     * @throws Exception Se ocorrer um erro durante a configuração.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()  // Desativa a proteção CSRF (Cross-Site Request Forgery).
            .authorizeRequests()
            .requestMatchers("/auth/**").permitAll()  // Permite acesso sem autenticação aos endpoints de autenticação.
            .requestMatchers("/cardapios/**", "/pedidos/**").hasRole("CLIENTE")  // Requer papel CLIENTE para acessar esses endpoints.
            .requestMatchers("/**").hasRole("ADMIN")  // Requer papel ADMIN para acessar qualquer outro endpoint.
            .anyRequest().authenticated()  // Exige autenticação para todas as outras requisições.
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);  // Define a política de criação de sessão como stateless (sem sessão).

        // Adiciona o filtro JWT antes do filtro de autenticação padrão do Spring Security.
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();  // Retorna a configuração final de segurança.
    }

    /**
     * Configura o gerenciador de autenticação com o serviço de login e codificador de senha.
     * @param auth O construtor de AuthenticationManagerBuilder.
     * @throws Exception Se ocorrer um erro durante a configuração.
     */
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);
        
        // Define o serviço de detalhes do usuário e o codificador de senha.
        authenticationManagerBuilder.userDetailsService(loginService)
            .passwordEncoder(passwordEncoder());
        
        return authenticationManagerBuilder.build();  // Retorna o AuthenticationManager configurado.
    }

    /**
     * Define um codificador de senha usando BCrypt.
     * @return Um objeto BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Cria e retorna um codificador de senha BCrypt.
    }
}
