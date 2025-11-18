package br.com.fiap.mentorai.dto.response;

import lombok.*;

import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HabilidadeResponse {
    private UUID id;
    private String nome;
    private UUID idCategoria;   // referência
    private String categoria;   // nome da categoria (conveniência)
    private String descricao;
}
