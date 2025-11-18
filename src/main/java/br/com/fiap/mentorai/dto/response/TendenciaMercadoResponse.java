package br.com.fiap.mentorai.dto.response;


import br.com.fiap.mentorai.model.enums.NivelDemandaEnum;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TendenciaMercadoResponse {
    private UUID id;
    private String descricao;
    private BigDecimal indiceDemanda; // ex.: 73.50
    private String fonte;
    private LocalDate dataAnalise;
    private NivelDemandaEnum nivelDemanda; // derivado no service a partir do índice
}
