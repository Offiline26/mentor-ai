package br.com.fiap.mentorai.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioRotaResponse {
    private Long idUsuario;
    private Long idRota;
    private String nomeRota;
    private BigDecimal progressoPercentual;
    private String status;
    private LocalDateTime dataInicio;
    private LocalDateTime dataConclusao;
}
