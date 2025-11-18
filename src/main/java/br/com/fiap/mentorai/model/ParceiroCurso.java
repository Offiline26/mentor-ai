package br.com.fiap.mentorai.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "parceiros_curso")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ParceiroCurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_parceiro")
    private UUID id;

    @Column(name = "nome_parceiro", nullable = false, unique = true, length = 150)
    private String nome;

    @Lob
    @Column(name = "descricao")
    private String descricao;
}
