package br.com.fiap.mentorai.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoginRequest {

    @NotBlank(message = "{usuario.email.notblank}")
    @Email(message = "{usuario.email.invalid}")
    @Size(max = 100)
    private String email;

    @NotBlank(message = "{usuario.password.notblank}")
    @Size(min = 8, max = 100)
    private String senha;
}
