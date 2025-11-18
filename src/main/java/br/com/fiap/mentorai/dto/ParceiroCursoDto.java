package br.com.fiap.mentorai.dto;

import lombok.*;

import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ParceiroCursoDto {
    private UUID id;
    private String nome;
    private String descricao;
}
