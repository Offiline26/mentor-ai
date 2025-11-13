package br.com.fiap.mentorai.dto.request.create;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateCategoriaHabilidadeRequest {
    private String nome;
    private String descricao;
}
