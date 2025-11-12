package br.com.fiap.mentorai.dto.request;

import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateRotaRequalificacaoRequest {
    private String nomeRota;
    private String descricao;
    private String objetivoProfissional;
    private Long idTendencia; // opcional
    private Boolean geradaPorIa;

    // definição de trilha (curso + ordem)
    @Builder.Default
    private List<RotaCursoItem> trilha = java.util.Collections.emptyList();

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class RotaCursoItem {
        private Long idCurso;
        private Integer ordem;
    }
}
