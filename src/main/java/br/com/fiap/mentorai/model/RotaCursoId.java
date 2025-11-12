package br.com.fiap.mentorai.model;

import lombok.*;

import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class RotaCursoId implements Serializable {
    private Long rota;
    private Long curso;
}
