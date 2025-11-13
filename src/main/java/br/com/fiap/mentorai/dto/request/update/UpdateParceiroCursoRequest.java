package br.com.fiap.mentorai.dto.request.update;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateParceiroCursoRequest {
    @Size(max = 150)
    private String nome;

    @Size(max = 4000)
    private String descricao;
}
