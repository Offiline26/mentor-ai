package br.com.fiap.mentorai.dto.request.create;

import br.com.fiap.mentorai.dto.upsert.UsuarioHabilidadeUpsert;
import br.com.fiap.mentorai.model.enums.GeneroEnum;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateUsuarioRequest {

    @NotBlank
    @Size(max = 100)
    private String nome;

    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(min = 8, max = 100)
    private String senha;

    @NotNull
    @Past
    private LocalDate dataNascimento;

    @NotNull
    private GeneroEnum genero;

    @NotBlank
    @Size(max = 50)
    private String pais;

    @NotNull
    private Long idCargo;

    @NotNull
    private Long idAreaAtuacao;

    private Set<UsuarioHabilidadeUpsert> habilidades;
}
