package br.com.fiap.mentorai.dto.response;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RotaRequalificacaoResponse {
    private UUID id;
    private String nomeRota;
    private String descricao;
    private String objetivoProfissional;
    private UUID idTendencia;

    // cursos em ordem
    @Builder.Default
    private List<RotaCursoStep> trilha = java.util.Collections.emptyList();

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class RotaCursoStep {
        private UUID idCurso;
        private Integer ordem;
        private String tituloCurso;
    }
}
