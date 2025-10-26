package co.edu.unbosque.microservice_investor.security;


import co.edu.unbosque.microservice_investor.model.dto.UserDTO;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {


    public String generateToken(UserDTO user) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("alpacaAccountId", user.getAlpacaAccountId());
        claims.put("userId", user.getId());
        claims.put("bankRelationshipId", user.getBankRelationshipId());
        claims.put("roleName", user.getRole());
        claims.put("stockBrokerId", null);
        claims.put("name", user.getName());
        // Todo: Cuando se tengan comisionistas, agregar aqui y en los parametros



        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().parseClaimsJwt(token).getBody();
    }

    public Integer extractUserId(String token) {
        try {
            Claims claims = Jwts.parser().parseClaimsJwt(token).getBody();
            return (Integer) claims.get("userId");
        } catch (Exception e) {
            throw new JwtTokenException("Error al extraer el userId del token.");
        }
    }

    public class JwtTokenException extends RuntimeException {
        public JwtTokenException(String message) {
            super(message);
        }
    }

}