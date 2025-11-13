package br.com.fiap.mentorai.security.autenticacao;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;

@Service
@ConditionalOnProperty(name = "app.jwt.secret")
public class JwtService {

    private final String secretBase64;
    private final long expMinutes;
    private SecretKey key;

    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.exp-min:120}") long expMinutes) {
        this.secretBase64 = secret;
        this.expMinutes = expMinutes;
    }

    @PostConstruct
    void initKey() {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretBase64));
    }

    public String generate(String subject, Collection<? extends GrantedAuthority> roles) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(subject) // email
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
        var claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return user.getUsername().equalsIgnoreCase(claims.getSubject())
                && claims.getExpiration().toInstant().isAfter(Instant.now());
    }
}
