package br.com.fiap.mentorai.dto.request.update;

import br.com.fiap.mentorai.dto.request.create.CreateRotaRequalificacaoRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateRotaRequalificacaoRequest {

    @Size(max = 150)
    private String nomeRota;

    @Size(max = 4000)
    private String descricao;

    @Size(max = 150)
    private String objetivoProfissional;

    // opcional, sem @NotNull porque é update
    private UUID idTendencia;

    @Valid
    private List<CreateRotaRequalificacaoRequest.RotaCursoItem> trilha;
}