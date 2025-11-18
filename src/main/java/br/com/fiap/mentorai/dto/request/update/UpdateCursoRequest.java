package br.com.fiap.mentorai.dto.request.update;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateCursoRequest {

    @Size(max = 150)
    private String titulo;

    @Size(max = 4000)
    private String descricao;

    @Positive
    private BigDecimal duracaoHoras;

    private UUID idParceiro;

    @Size(max = 255)
    @URL
    private String linkCurso;

    private UUID idCategoriaCurso;

    // IDs de habilidades — nenhum obrigatório
    private Set<UUID> habilidades;
}
