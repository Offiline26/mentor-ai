package br.com.fiap.mentorai.dto.request;

import br.com.fiap.mentorai.dto.upsert.UsuarioHabilidadeUpsert;
import br.com.fiap.mentorai.model.enums.GeneroEnum;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateUsuarioRequest {
    private String nome;
    private String email;
    private String senha; // entra em texto; service gera hash
    private LocalDate dataNascimento;
    private GeneroEnum genero; // opcional
    private String pais;
    private Long idCargo;
    private Long idAreaAtuacao;

    // opcional: habilidades iniciais
    private Set<UsuarioHabilidadeUpsert> habilidades;
}
