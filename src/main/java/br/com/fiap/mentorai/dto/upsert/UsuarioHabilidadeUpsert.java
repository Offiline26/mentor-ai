package br.com.fiap.mentorai.dto.upsert;

import lombok.*;

import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioHabilidadeUpsert {
    private UUID idHabilidade;
    private Integer nivel; // 1..5
}
