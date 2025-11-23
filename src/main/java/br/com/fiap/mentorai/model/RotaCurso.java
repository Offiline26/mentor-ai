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
    @JsonIgnore // 🛑 CORREÇÃO CRÍTICA: Quebra o loop infinito do JSON
    @ToString.Exclude // Evita loop no toString()
    private RotaRequalificacao rota;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_curso")
    private Curso curso;

    @Column(name = "ordem")
    private Integer ordem;
}
