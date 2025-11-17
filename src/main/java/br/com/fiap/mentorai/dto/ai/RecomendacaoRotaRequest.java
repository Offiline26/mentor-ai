package br.com.fiap.mentorai.dto.ai;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RecomendacaoRotaRequest {
    private Long idUsuario;                    // opcional, se quiser puxar do banco
    private String nomeUsuario;                // se vier do front
    private String objetivoProfissional;
    private String cargoAtual;
    private String areaAtuacao;
    private List<String> habilidadesAtuais;
    private List<String> habilidadesDesejadas;
    private Integer horasPorSemana;            // opcional, ajuda na recomendação
}
