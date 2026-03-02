package com.ifsc.ctds.stinghen.recycle_it_api.security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.nio.charset.StandardCharsets;

/**
 * Utilitário para extração de informações do token JWT
 * @version 1.0
 * @since 01/03/2026
 * @author Gustavo Stinghen
 */
public class KeycloakRoleExtractor extends {

    private static final String SECRET_KEY = "sua-chave-secreta-aqui"; // Configure sua chave secreta
    
    /**
     * Extrai o email do token JWT
     * @param jwtToken O token JWT
     * @return O email extraído do token
     */
    public static String extractEmail(String jwtToken) {
        try {
            JwtDecoder jwtDecoder = JwtDecoder.withSecretKey(
                    Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                    .build();
            
            Jwt jwt = jwtDecoder.decode(jwtToken);
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();
            
            return claims.getSubject(); // Geralmente o email está no subject
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Extrai o email do objeto Jwt
     * @param jwt O objeto Jwt
     * @return O email extraído do token
     */
    public static String extractEmail(Jwt jwt) {
        try {
            if (jwt != null && jwt.getSubject() != null) {
                return jwt.getSubject();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Extrai as roles do token JWT
     * @param jwtToken O token JWT
     * @return As roles extraídas do token
     */
    public static String[] extractRoles(String jwtToken) {
        try {
            JwtDecoder jwtDecoder = JwtDecoder.withSecretKey(
                    Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                    .build();
            
            Jwt jwt = jwtDecoder.decode(jwtToken);
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();
            
            Object rolesClaim = claims.get("roles");
            if (rolesClaim instanceof String[]) {
                return (String[]) rolesClaim;
            } else if (rolesClaim instanceof String) {
                return new String[]{(String) rolesClaim};
            }
            return new String[]{};
        } catch (Exception e) {
            return new String[]{};
        }
    }
    
    /**
     * Verifica se o usuário tem uma role específica
     * @param jwtToken O token JWT
     * @param role A role a ser verificada
     * @return true se o usuário tiver a role, false caso contrário
     */
    public static boolean hasRole(String jwtToken, String role) {
        String[] roles = extractRoles(jwtToken);
        for (String userRole : roles) {
            if (userRole.equals(role)) {
                return true;
            }
        }
        return false;
    }
}
