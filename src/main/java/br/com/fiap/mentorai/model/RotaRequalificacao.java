package br.com.fiap.mentorai.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "rotas_requalificacao")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RotaRequalificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rota")
    private UUID id;

    @Column(name = "nome_rota", nullable = false, length = 150)
    private String nomeRota;

    @Lob
    private String descricao;

    @Column(name = "objetivo_profissional", length = 150)
    private String objetivoProfissional;

    @ManyToOne
    @JoinColumn(name = "id_tendencia")
    private TendenciaMercado tendencia;

    @OneToMany(mappedBy = "rota", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ordem ASC")
    @Builder.Default
    @ToString.Exclude
    private Set<RotaCurso> cursos = new LinkedHashSet<>();

    public void adicionarCurso(Curso curso, Integer ordem) {
        RotaCurso rc = RotaCurso.builder()
                .rota(this)
                .curso(curso)
                .ordem(ordem)
                .build();
        cursos.add(rc);
    }
}
