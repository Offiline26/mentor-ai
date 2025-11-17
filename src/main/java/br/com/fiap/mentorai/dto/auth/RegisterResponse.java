package br.com.fiap.mentorai.dto.auth;

import br.com.fiap.mentorai.model.enums.GeneroEnum;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponse {

    private Long idUsuario;
    private String nome;
    private String email;
    private LocalDate dataNascimento;
    private GeneroEnum genero;
    private String pais;

    private Long idCargo;
    private String cargo;

    private Long idAreaAtuacao;
    private String areaAtuacao;

    private LocalDateTime dataCadastro;
}
