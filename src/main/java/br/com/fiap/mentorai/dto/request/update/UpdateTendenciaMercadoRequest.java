package br.com.fiap.mentorai.dto.request.update;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateTendenciaMercadoRequest {
    private String descricao;
    private BigDecimal indiceDemanda;
    private String fonte;
    private LocalDate dataAnalise;
}
