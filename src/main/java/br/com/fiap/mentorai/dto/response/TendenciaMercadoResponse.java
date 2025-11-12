package br.com.fiap.mentorai.dto.response;


import br.com.fiap.mentorai.model.enums.NivelDemandaEnum;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TendenciaMercadoResponse {
    private Long id;
    private String descricao;
    private BigDecimal indiceDemanda; // ex.: 73.50
    private String fonte;
    private LocalDate dataAnalise;
    private NivelDemandaEnum nivelDemanda; // derivado no service a partir do índice
}
