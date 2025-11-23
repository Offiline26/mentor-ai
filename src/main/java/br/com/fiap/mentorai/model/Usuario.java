package br.com.fiap.mentorai.model;


import br.com.fiap.mentorai.model.converters.GeneroConverter;
import br.com.fiap.mentorai.model.enums.Genero;
import br.com.fiap.mentorai.model.enums.NivelProficienciaEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "usuarios")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "senha_hash", nullable = false, length = 255)
    private String senha;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Convert(converter = GeneroConverter.class)
    @Column(name = "genero")
    private Genero genero;

    @Column(length = 50)
    private String pais;

    @ManyToOne
    @JoinColumn(name = "id_cargo", nullable = false)
    private Cargo cargo;

    @ManyToOne
    @JoinColumn(name = "id_area", nullable = false)
    private AreaAtuacao areaAtuacao;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    // 🛑 MUDANÇA CRÍTICA: Definir FetchType.LAZY
    @Builder.Default
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuarioHabilidade> habilidades = new ArrayList<>();

    // 🛑 MUDANÇA CRÍTICA: Definir FetchType.LAZY
    @Builder.Default
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UsuarioRota> rotas = new HashSet<>();

    @PrePersist
    void prePersist() {
        if (dataCadastro == null) {
            dataCadastro = LocalDateTime.now();
        }
    }



    // ---------- Métodos de domínio ----------

    public void adicionarHabilidade(Habilidade habilidade, NivelProficienciaEnum nivel) {
        UsuarioHabilidade uh = UsuarioHabilidade.builder()
                .usuario(this)
                .habilidade(habilidade)
                .nivelProficiencia(nivel)
                .build();
        habilidades.add(uh);
    }

    public UsuarioRota iniciarRota(RotaRequalificacao rota) {
        UsuarioRota usuarioRota = UsuarioRota.builder()
                .usuario(this)
                .rota(rota)
                .progressoPercentual(java.math.BigDecimal.ZERO)
                .dataInicio(LocalDateTime.now())
                .build();
        rotas.add(usuarioRota);
        return usuarioRota;
    }
}
