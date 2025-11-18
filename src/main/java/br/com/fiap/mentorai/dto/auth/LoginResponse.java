package br.com.fiap.mentorai.dto.auth;

import lombok.*;

import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoginResponse {

    private UUID idUsuario;
    private String nome;
    private String email;
}
