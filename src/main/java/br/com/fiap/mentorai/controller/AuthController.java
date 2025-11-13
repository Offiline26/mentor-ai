package br.com.fiap.mentorai.controller;

import br.com.fiap.mentorai.dto.request.LoginRequest;
import br.com.fiap.mentorai.dto.response.LoginResponse;
import br.com.fiap.mentorai.exception.ResourceNotFoundException;
import br.com.fiap.mentorai.model.Usuario;
import br.com.fiap.mentorai.repository.UsuarioRepository;
import br.com.fiap.mentorai.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager,
                          UsuarioRepository usuarioRepository,
                          JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> apiLogin(@Valid @RequestBody LoginRequest req) {

        try {
            final String email = req.getEmail().trim().toLowerCase(Locale.ROOT);

            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, req.getPassword())
            );

            UserDetails principal = (UserDetails) auth.getPrincipal();

            String token = jwtService.generate(principal.getUsername(), principal.getAuthorities());

            Usuario u = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

            LoginResponse userInfo = LoginResponse.builder()
                    .idUsuario(u.getId())
                    .nome(u.getNome())
                    .email(u.getEmail())
                    .build();

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("accessToken", token);
            body.put("tokenType", "Bearer");
            body.put("user", userInfo);

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .body(body);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "E-mail ou senha inválidos"));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal UserDetails principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Não autenticado"));
        }

        String email = principal.getUsername();
        Usuario u = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        LoginResponse userInfo = LoginResponse.builder()
                .idUsuario(u.getId())
                .nome(u.getNome())
                .email(u.getEmail())
                .build();

        return ResponseEntity.ok(userInfo);
    }
}
