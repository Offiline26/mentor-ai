package br.com.fiap.mentorai.model;

import lombok.*;

import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class UsuarioHabilidadeId implements Serializable {
    private Long usuario;
    private Long habilidade;
}
