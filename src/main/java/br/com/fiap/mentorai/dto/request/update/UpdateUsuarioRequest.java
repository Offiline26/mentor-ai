package br.com.fiap.mentorai.dto.request.update;
import br.com.fiap.mentorai.dto.upsert.UsuarioHabilidadeUpsert;
import br.com.fiap.mentorai.model.enums.GeneroEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateUsuarioRequest {

    @Size(max = 100)
    private String nome;

    @Email
    @Size(max = 100)
    private String email;

    @Past
    private LocalDate dataNascimento;

    private GeneroEnum genero;

    @Size(max = 50)
    private String pais;

    private Long idCargo;

    private Long idAreaAtuacao;

    @Valid
    private Set<UsuarioHabilidadeUpsert> habilidades;
}
