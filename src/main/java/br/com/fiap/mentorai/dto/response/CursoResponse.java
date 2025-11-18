package br.com.fiap.mentorai.dto.response;


import lombok.*;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CursoResponse {
    private UUID id;
    private String titulo;
    private String descricao;
    private BigDecimal duracaoHoras;
    private UUID idParceiro;
    private String parceiro;
    private String linkCurso;
    private UUID idCategoriaCurso;
    private String categoriaCurso;
    private Set<UUID> habilidades; // ids das habilidades atreladas
}
