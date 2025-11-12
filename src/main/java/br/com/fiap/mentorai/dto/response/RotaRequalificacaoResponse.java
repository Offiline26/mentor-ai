package br.com.fiap.mentorai.dto.response;

import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RotaRequalificacaoResponse {
    private Long id;
    private String nomeRota;
    private String descricao;
    private String objetivoProfissional;
    private Long idTendencia;
    private Boolean geradaPorIa;

    // cursos em ordem
    @Builder.Default
    private List<RotaCursoStep> trilha = java.util.Collections.emptyList();

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class RotaCursoStep {
        private Long idCurso;
        private Integer ordem;
        private String tituloCurso;
    }
}
