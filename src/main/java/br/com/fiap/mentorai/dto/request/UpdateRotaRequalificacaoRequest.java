package br.com.fiap.mentorai.dto.request;

import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateRotaRequalificacaoRequest {
    private String nomeRota;
    private String descricao;
    private String objetivoProfissional;
    private Long idTendencia;
    private Boolean geradaPorIa;
    private List<CreateRotaRequalificacaoRequest.RotaCursoItem> trilha;
}
