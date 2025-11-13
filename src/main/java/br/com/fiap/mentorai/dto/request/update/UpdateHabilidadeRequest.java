package br.com.fiap.mentorai.dto.request.update;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateHabilidadeRequest {
    @Size(max = 100)
    private String nome;

    // opcional no update — sem @NotNull
    private Long idCategoria;

    @Size(max = 4000)
    private String descricao;
}
