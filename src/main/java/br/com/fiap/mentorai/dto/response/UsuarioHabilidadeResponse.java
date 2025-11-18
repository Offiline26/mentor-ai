package br.com.fiap.mentorai.dto.response;

import lombok.*;

import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioHabilidadeResponse {
    private UUID idUsuario;
    private UUID idHabilidade;
    private String habilidade;
    private Integer nivel; // 1..5
}
