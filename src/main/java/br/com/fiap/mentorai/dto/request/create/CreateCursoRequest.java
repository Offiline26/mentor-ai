package br.com.fiap.mentorai.dto.request.create;

import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateCursoRequest {
    private String titulo;
    private String descricao;
    private BigDecimal duracaoHoras;
    private Long idParceiro;
    private String linkCurso;
    private Long idCategoriaCurso;
    private Set<Long> habilidades; // ids
}

