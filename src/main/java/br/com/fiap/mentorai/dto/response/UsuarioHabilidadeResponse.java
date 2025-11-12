package br.com.fiap.mentorai.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioHabilidadeResponse {
    private Long idUsuario;
    private Long idHabilidade;
    private String habilidade;
    private Integer nivel; // 1..5
}
