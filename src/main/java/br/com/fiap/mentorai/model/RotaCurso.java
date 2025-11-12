package br.com.fiap.mentorai.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rota_curso")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@IdClass(RotaCursoId.class)
public class RotaCurso {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_rota")
    private RotaRequalificacao rota;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_curso")
    private Curso curso;

    @Column(name = "ordem")
    private Integer ordem;
}
