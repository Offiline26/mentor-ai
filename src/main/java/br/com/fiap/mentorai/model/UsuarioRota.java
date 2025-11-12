package br.com.fiap.mentorai.model;

import br.com.fiap.mentorai.model.enums.StatusRotaEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuario_rota")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@IdClass(UsuarioRotaId.class)
public class UsuarioRota {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_rota")
    private RotaRequalificacao rota;

    @Column(name = "progresso_percentual", precision = 5, scale = 2)
    private BigDecimal progressoPercentual;

    @Column(name = "data_inicio")
    private LocalDateTime dataInicio;

    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;

    @Transient
    public StatusRotaEnum getStatus() {
        if (dataInicio == null) {
            return StatusRotaEnum.NAO_INICIADA;
        }
        if (dataConclusao != null
                || (progressoPercentual != null && progressoPercentual.compareTo(new BigDecimal("100.00")) >= 0)) {
            return StatusRotaEnum.CONCLUIDA;
        }
        return StatusRotaEnum.EM_ANDAMENTO;
    }
}
