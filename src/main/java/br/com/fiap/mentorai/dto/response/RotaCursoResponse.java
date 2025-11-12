package br.com.fiap.mentorai.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RotaCursoResponse {
    private Long idRota;
    private Long idCurso;
    private Integer ordem;
    private String tituloCurso;
}