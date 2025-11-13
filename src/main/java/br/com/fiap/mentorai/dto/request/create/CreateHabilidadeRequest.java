package br.com.fiap.mentorai.dto.request.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateHabilidadeRequest {
    @NotBlank
    @Size(max = 100)
    private String nome;

    // opcional no create (pode ser null)
    private Long idCategoria;

    @Size(max = 4000)
    private String descricao;
}

