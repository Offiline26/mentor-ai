package br.com.fiap.mentorai.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "categorias_curso")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CategoriaCurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cat_curso")
    private UUID id;

    @Column(name = "nome_cat_curso", nullable = false, unique = true, length = 100)
    private String nome;

    @Lob
    @Column(name = "descricao")
    private String descricao;
}
