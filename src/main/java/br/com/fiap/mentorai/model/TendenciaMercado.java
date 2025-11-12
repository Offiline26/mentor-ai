package br.com.fiap.mentorai.model;

import br.com.fiap.mentorai.model.enums.NivelDemandaEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tendencias_mercado")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TendenciaMercado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tendencia")
    private Long id;

    @Lob
    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "indice_demanda", precision = 6, scale = 2)
    private BigDecimal indiceDemanda;

    @Column(name = "fonte", length = 100)
    private String fonte;

    @Column(name = "data_analise")
    private LocalDate dataAnalise;

    @Transient
    public NivelDemandaEnum getNivelDemanda() {
        if (indiceDemanda == null) {
            return null;
        }
        double v = indiceDemanda.doubleValue();
        if (v < 25.0) {
            return NivelDemandaEnum.BAIXA;
        } else if (v < 50.0) {
            return NivelDemandaEnum.MEDIA;
        } else if (v < 75.0) {
            return NivelDemandaEnum.ALTA;
        }
        return NivelDemandaEnum.MUITO_ALTA;
    }
}
