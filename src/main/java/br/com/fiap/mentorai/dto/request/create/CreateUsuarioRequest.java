package br.com.fiap.mentorai.dto.request.create;

import br.com.fiap.mentorai.dto.upsert.UsuarioHabilidadeUpsert;
import br.com.fiap.mentorai.model.enums.Genero;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateUsuarioRequest {

    @NotBlank(message = "{usuario.nome.notblank}")
    @Size(max = 100)
    private String nome;

    @NotBlank(message = "{usuario.email.notblank}")
    @Email(message = "{usuario.email.invalid}")
    @Size(max = 100)
    private String email;

    @NotBlank(message = "{usuario.password.notblank}")
    @Size(min = 8, max = 100)
    private String senha;

    @NotNull
    @Past
    private LocalDate dataNascimento;

    @NotNull
    private Genero genero;

    @NotBlank
    @Size(max = 50)
    private String pais;

    @NotNull
    private UUID idCargo;

    @NotNull
    private UUID idAreaAtuacao;

    private Set<UsuarioHabilidadeUpsert> habilidades;
}
