package br.com.fiap.mentorai.dto;


import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CategoriaCursoDto {
    private Long id;
    private String nome;
    private String descricao;
}