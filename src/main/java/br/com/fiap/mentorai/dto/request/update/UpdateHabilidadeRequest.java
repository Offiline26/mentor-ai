package br.com.fiap.mentorai.dto.request.update;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateHabilidadeRequest {
    private String nome;
    private Long idCategoria;
    private String descricao;
}
