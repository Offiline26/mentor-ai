package br.com.fiap.mentorai.dto.auth;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoginResponse {

    private Long idUsuario;
    private String nome;
    private String email;
}
