package br.com.fiap.mentorai.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "areas_atuacao")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AreaAtuacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_area")
    private Long id;

    @Column(name = "nome_area", nullable = false, unique = true, length = 100)
    private String nome;

    @Lob
    @Column(name = "descricao")
    private String descricao;
}
