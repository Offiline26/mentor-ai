package br.com.fiap.mentorai.dto.upsert;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioHabilidadeUpsert {
    private Long idHabilidade;
    private Integer nivel; // 1..5
}
