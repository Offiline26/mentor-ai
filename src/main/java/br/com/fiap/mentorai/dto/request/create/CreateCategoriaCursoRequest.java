package br.com.fiap.mentorai.dto.request.create;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateCategoriaCursoRequest {
    private String nome;
    private String descricao;
}
