package br.com.fiap.mentorai.messaging;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RotaCriadaEvent {

    private Long idRota;
    private String nomeRota;
    private String objetivoProfissional;
    private Boolean geradaPorIa;
    private LocalDateTime dataCriacao;

    // opcional: ids dos cursos da trilha
    private List<Long> cursos;
}
