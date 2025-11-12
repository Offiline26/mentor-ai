package br.com.fiap.mentorai.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "habilidades")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Habilidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_habilidade")
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "id_cat_hab")
    private CategoriaHabilidade categoria;

    @Lob
    @Column(name = "descricao")
    private String descricao;
}
