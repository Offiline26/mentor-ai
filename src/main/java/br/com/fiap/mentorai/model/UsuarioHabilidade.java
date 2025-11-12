package br.com.fiap.mentorai.model;


import br.com.fiap.mentorai.model.converters.NivelProficienciaConverter;
import br.com.fiap.mentorai.model.enums.NivelProficienciaEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario_habilidade")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@IdClass(UsuarioHabilidadeId.class)
public class UsuarioHabilidade {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_habilidade")
    private Habilidade habilidade;

    @Convert(converter = NivelProficienciaConverter.class)
    @Column(name = "nivel_proficiencia")
    private NivelProficienciaEnum nivelProficiencia;
}
