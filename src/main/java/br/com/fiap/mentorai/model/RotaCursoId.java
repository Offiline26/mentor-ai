package br.com.fiap.mentorai.model;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class RotaCursoId implements Serializable {
    private UUID rota;
    private UUID curso;
}
