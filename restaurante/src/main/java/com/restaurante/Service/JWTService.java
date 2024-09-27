package com.restaurante.Service;

import com.restaurante.model.Login;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm; 
import java.util.Date;
import java.util.List;


@Service
public class JWTService {

    /**
     * Chave secreta usada para assinar os tokens JWT.
     * A chave é lida a partir das propriedades da aplicação.
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Tempo de expiração dos tokens JWT, lido a partir das propriedades da aplicação.
     */
    @Value("${jwt.expiration}")
    private long expirationTime;

    /**
     * Gera um token JWT para o usuário especificado.
     * @param login O objeto Login contendo as informações do usuário.
     * @return O token JWT gerado.
     */
    public String generateToken(Login login) {
        return Jwts.builder() // Cria o construtor do token
                .setSubject(login.getUsername()) // Define o nome de usuário como o assunto do token
                .claim("roles", login.getUser().getRoles().stream() // Adiciona os papéis do usuário ao token
                    .map(role -> role.getNome()) // Mapeia os papéis para seus nomes
                    .toList()) // Coleta em uma lista
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // Define a data de expiração do token
                .signWith(SignatureAlgorithm.HS512, secret) // Assina o token com o algoritmo HS512 e a chave secreta
                .compact(); // Gera o token compactado
    }

    /**
     * Valida se o token JWT é válido.
     * @param token O token JWT a ser validado.
     * @return true se o token for válido, false caso contrário.
     */
    public boolean validateToken(String token) {
        try {
            // Tenta analisar o token usando a chave secreta
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true; // Retorna true se o token é válido
        } catch (Exception e) {
            // Se ocorrer qualquer exceção durante a análise, o token é considerado inválido
            return false;
        }
    }

    /**
     * Extrai o nome de usuário do token JWT.
     * @param token O token JWT do qual extrair o nome de usuário.
     * @return O nome de usuário extraído do token.
     */
    public String getUsernameFromToken(String token) {
        // Obtém os detalhes do token e extrai o assunto (nome de usuário)
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claims.getSubject(); // Retorna o nome de usuário
    }

    /**
     * Extrai os papéis do usuário do token JWT.
     * @param token O token JWT do qual extrair os papéis.
     * @return A lista de papéis extraídos do token.
     */
    public List<String> getRolesFromToken(String token) {
        // Obtém os detalhes do token e extrai os papéis
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claims.get("roles", List.class); // Retorna a lista de papéis
    }

    /**
     * Extrai o token JWT do cabeçalho da requisição HTTP.
     * @param request A requisição HTTP que contém o cabeçalho Authorization.
     * @return O token JWT extraído, ou null se não houver token.
     */
    public String getTokenFromRequest(HttpServletRequest request) {
        // Obtém o cabeçalho Authorization da requisição
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            // Remove o prefixo "Bearer " e retorna o token
            return bearerToken.substring(7);
        }
        return null; // Retorna null se o cabeçalho não contiver um token JWT
    }
}
