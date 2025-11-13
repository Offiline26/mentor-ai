package br.com.fiap.mentorai.dto.request.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateParceiroCursoRequest {

    @NotBlank
    @Size(max = 100)
    private String nome;

    @NotBlank
    @Size(max = 4000)
    private String descricao;
}
