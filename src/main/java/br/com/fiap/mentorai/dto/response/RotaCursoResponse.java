package br.com.fiap.mentorai.dto.response;

import lombok.*;

import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RotaCursoResponse {
    private UUID idRota;
    private UUID idCurso;
    private Integer ordem;
    private String tituloCurso;
}