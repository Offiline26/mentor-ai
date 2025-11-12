package br.com.fiap.mentorai.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ParceiroCursoDto {
    private Long id;
    private String nome;
    private String descricao;
}
