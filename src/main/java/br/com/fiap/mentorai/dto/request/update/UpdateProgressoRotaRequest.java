package br.com.fiap.mentorai.dto.request.update;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateProgressoRotaRequest {

    @NotNull(message = "O progresso é obrigatório")
    @DecimalMin(value = "0.00", message = "O mínimo é 0%")
    @DecimalMax(value = "100.00", message = "O máximo é 100%")
    private BigDecimal progresso;
}
