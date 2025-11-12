package br.com.fiap.mentorai.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateParceiroCursoRequest {
    private String nome;
    private String descricao;
}
