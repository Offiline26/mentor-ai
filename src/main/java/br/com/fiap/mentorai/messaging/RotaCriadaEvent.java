package br.com.fiap.mentorai.messaging;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RotaCriadaEvent {

    private UUID idRota;
    private String nomeRota;
    private String objetivoProfissional;
    private LocalDateTime dataCriacao;

    // opcional: ids dos cursos da trilha
    private List<UUID> cursos;
}
