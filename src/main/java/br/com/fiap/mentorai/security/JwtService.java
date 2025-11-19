package br.com.fiap.mentorai.security;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;

@Service
public class JwtService {

    // Valor default apenas para garantir que sobe em DEV se esquecer a config
    // Em PROD, isso deve ser sobrescrito via variável de ambiente
    private final String secretBase64;
    private final long expMinutes;
    private SecretKey key;

    public JwtService(
            @Value("${app.jwt.secret:OpX/0+1234567890abcdef1234567890abcdef123456=}") String secret,
            @Value("${app.jwt.exp-min:120}") long expMinutes) {
        this.secretBase64 = secret;
        this.expMinutes = expMinutes;
    }

    @PostConstruct
    void initKey() {
        // Garante decodificação segura, suportando chaves em Base64 ou Plain Text longo
        try {
            byte[] decodedKey = Base64.getDecoder().decode(secretBase64);
            this.key = Keys.hmacShaKeyFor(decodedKey);
        } catch (IllegalArgumentException e) {
            // Fallback se a string não for Base64 válida, usa bytes diretos (não recomendado para prod, mas útil em dev)
            this.key = Keys.hmacShaKeyFor(secretBase64.getBytes(StandardCharsets.UTF_8));
        }
    }

    public String generate(String subject, Collection<? extends GrantedAuthority> roles) {
        var now = Instant.now();
        return Jwts.builder()
                .subject(subject)
                .claim("roles", roles.stream().map(GrantedAuthority::getAuthority).toList())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expMinutes, ChronoUnit.MINUTES)))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public String extractSubject(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isValid(String token, UserDetails user) {
        try {
            var claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return user.getUsername().equalsIgnoreCase(claims.getSubject())
                    && claims.getExpiration().toInstant().isAfter(Instant.now());
        } catch (Exception e) {
            return false;
        }
    }
}
