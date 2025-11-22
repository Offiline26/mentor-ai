package br.com.fiap.mentorai.dto.request.create;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateRotaRequalificacaoRequest {

    @NotBlank
    @Size(max = 150)
    private String nomeRota;

    @Size(max = 4000)
    private String descricao;

    @Size(max = 150)
    private String objetivoProfissional;

    // opcional
    private UUID idTendencia;

    @Valid
    @Builder.Default
    private List<RotaCursoItem> trilha = new ArrayList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RotaCursoItem {

        @NotNull
        private UUID idCurso;

        @NotNull
        @Positive
        private Integer ordem;
    }
}
