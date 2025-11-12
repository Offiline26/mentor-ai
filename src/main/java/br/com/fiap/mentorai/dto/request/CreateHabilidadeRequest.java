package br.com.fiap.mentorai.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateHabilidadeRequest {
    private String nome;
    private Long idCategoria; // FK
    private String descricao;
}

