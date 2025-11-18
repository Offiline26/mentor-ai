package br.com.fiap.mentorai.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioRotaResponse {
    private UUID idUsuario;
    private UUID idRota;
    private String nomeRota;
    private BigDecimal progressoPercentual;
    private String status;
    private LocalDateTime dataInicio;
    private LocalDateTime dataConclusao;
}
