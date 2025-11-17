package br.com.fiap.mentorai.dto.auth;


import br.com.fiap.mentorai.model.enums.GeneroEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank(message = "{usuario.nome.notblank}")
    @Size(max = 100)
    private String nome;

    @NotBlank(message = "{usuario.email.notblank}")
    @Email(message = "{usuario.email.invalid}")
    @Size(max = 100)
    private String email;

    @NotBlank(message = "{usuario.senha.notblank}")
    @Size(min = 8, max = 100, message = "{usuario.senha.size}")
    private String senha;

    @Past(message = "{usuario.dataNascimento.past}")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;

    private GeneroEnum genero;

    @Size(max = 50)
    private String pais;

    @NotNull(message = "{usuario.cargo.notnull}")
    private Long idCargo;

    @NotNull(message = "{usuario.area.notnull}")
    private Long idAreaAtuacao;
}
