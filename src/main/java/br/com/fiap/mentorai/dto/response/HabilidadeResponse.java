package br.com.fiap.mentorai.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HabilidadeResponse {
    private Long id;
    private String nome;
    private Long idCategoria;   // referência
    private String categoria;   // nome da categoria (conveniência)
    private String descricao;
}
