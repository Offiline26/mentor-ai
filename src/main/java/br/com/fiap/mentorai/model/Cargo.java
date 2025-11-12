package br.com.fiap.mentorai.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cargos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cargo")
    private Long id;

    @Column(name = "nome_cargo", nullable = false, unique = true, length = 100)
    private String nome;

    @Lob
    @Column(name = "descricao")
    private String descricao;
}
