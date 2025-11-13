package br.com.fiap.mentorai.dto.request.update;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateTendenciaMercadoRequest {
    @Size(max = 4000)
    private String descricao;

    @DecimalMin("0.0")
    @DecimalMax("100.0")
    private BigDecimal indiceDemanda;

    @Size(max = 100)
    private String fonte;

    @PastOrPresent
    private LocalDate dataAnalise;
}
