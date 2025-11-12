package br.com.fiap.mentorai.dto.response;


import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CursoResponse {
    private Long id;
    private String titulo;
    private String descricao;
    private BigDecimal duracaoHoras;
    private Long idParceiro;
    private String parceiro;
    private String linkCurso;
    private Long idCategoriaCurso;
    private String categoriaCurso;
    private Set<Long> habilidades; // ids das habilidades atreladas
}
