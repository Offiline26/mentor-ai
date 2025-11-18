package br.com.fiap.mentorai.dto.request.update;
import br.com.fiap.mentorai.dto.upsert.UsuarioHabilidadeUpsert;
import br.com.fiap.mentorai.model.enums.Genero;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateUsuarioRequest {

    @Size(max = 100)
    private String nome;

    @Email
    @Size(max = 100)
    private String email;

    @Past
    private LocalDate dataNascimento;

    private Genero genero;

    @Size(max = 50)
    private String pais;

    private UUID idCargo;

    private UUID idAreaAtuacao;

    @Valid
    private Set<UsuarioHabilidadeUpsert> habilidades;
}
