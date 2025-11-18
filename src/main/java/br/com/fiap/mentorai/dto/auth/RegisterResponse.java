package br.com.fiap.mentorai.dto.auth;

import br.com.fiap.mentorai.model.enums.Genero;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponse {

    private UUID idUsuario;
    private String nome;
    private String email;
    private LocalDate dataNascimento;
    private Genero genero;
    private String pais;

    private UUID idCargo;
    private String cargo;

    private UUID idAreaAtuacao;
    private String areaAtuacao;

    private LocalDateTime dataCadastro;
}
