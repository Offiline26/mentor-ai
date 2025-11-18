package br.com.fiap.mentorai.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "cursos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso")
    private UUID id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Lob
    @Column(name = "descricao")
    private String descricao;

    @Column(name = "duracao_horas", precision = 4, scale = 1)
    private BigDecimal duracaoHoras;

    @ManyToOne
    @JoinColumn(name = "id_parceiro")
    private ParceiroCurso parceiro;

    @Column(name = "link_curso", length = 255)
    private String linkCurso;

    @ManyToOne
    @JoinColumn(name = "id_cat_curso")
    private CategoriaCurso categoriaCurso;

    @ManyToMany
    @JoinTable(
            name = "curso_habilidade",
            joinColumns = @JoinColumn(name = "id_curso"),
            inverseJoinColumns = @JoinColumn(name = "id_habilidade")
    )
    @Builder.Default
    private Set<Habilidade> habilidades = new HashSet<>();
}
