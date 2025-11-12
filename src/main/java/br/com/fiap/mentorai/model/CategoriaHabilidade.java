package br.com.fiap.mentorai.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categorias_habilidade")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CategoriaHabilidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cat_hab")
    private Long id;

    @Column(name = "nome_cat_hab", nullable = false, unique = true, length = 100)
    private String nome;

    @Lob
    @Column(name = "descricao")
    private String descricao;
}