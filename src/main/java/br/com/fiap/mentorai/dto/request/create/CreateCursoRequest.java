package br.com.fiap.mentorai.dto.request.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateCursoRequest {

    @NotBlank
    @Size(max = 150)
    private String titulo;

    @Size(max = 4000)
    private String descricao;

    @NotNull
    @Positive
    private BigDecimal duracaoHoras;

    private Long idParceiro;

    @Size(max = 255)
    private String linkCurso; // se quiser: @URL

    private Long idCategoriaCurso;

    // IDs de habilidades relacionadas ao curso (opcional)
    private Set<Long> habilidades;
}

