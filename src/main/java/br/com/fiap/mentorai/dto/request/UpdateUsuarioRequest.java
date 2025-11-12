package br.com.fiap.mentorai.dto.request;
import br.com.fiap.mentorai.dto.upsert.UsuarioHabilidadeUpsert;
import br.com.fiap.mentorai.model.enums.GeneroEnum;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateUsuarioRequest {
    private String nome;
    private String email; // se for imutável, remova
    private LocalDate dataNascimento;
    private GeneroEnum genero;
    private String pais;
    private Long idCargo;
    private Long idAreaAtuacao;

    private Set<UsuarioHabilidadeUpsert> habilidades;
}
