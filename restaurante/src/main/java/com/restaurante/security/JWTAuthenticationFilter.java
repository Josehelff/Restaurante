package com.restaurante.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.restaurante.Service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * O filtro de autenticação JWT que estende OncePerRequestFilter.
 * Esse filtro é responsável por processar o token JWT em cada requisição.
 */
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Serviço JWT injetado pelo Spring.
     * Usado para extrair e validar tokens JWT.
     */
    @Autowired
    private JWTService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Obtém o token JWT da requisição usando o serviço JWT.
        String token = jwtService.getTokenFromRequest(request);

        // Se o token não for nulo e for válido...
        if (token != null && jwtService.validateToken(token)) {
            
            // Obtém o nome de usuário do token.
            String username = jwtService.getUsernameFromToken(token);

            // Cria um objeto de autenticação com o nome de usuário e os papéis extraídos do token.
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    username, null);

            // Adiciona detalhes adicionais da autenticação (como o IP do usuário).
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Define o contexto de segurança com a autenticação obtida.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Continua a cadeia de filtros, permitindo que a requisição prossiga.
        chain.doFilter(request, response);
    }
}
