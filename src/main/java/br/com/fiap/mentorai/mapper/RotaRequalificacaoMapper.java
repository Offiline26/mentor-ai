package br.com.fiap.mentorai.mapper;

import br.com.fiap.mentorai.dto.request.create.CreateRotaRequalificacaoRequest;
import br.com.fiap.mentorai.dto.response.RotaRequalificacaoResponse;
import br.com.fiap.mentorai.dto.request.update.UpdateRotaRequalificacaoRequest;
import br.com.fiap.mentorai.model.RotaCurso;
import br.com.fiap.mentorai.model.RotaRequalificacao;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class RotaRequalificacaoMapper {

    private RotaRequalificacaoMapper() {}

    // -------- Entidade -> DTO --------
    public static RotaRequalificacaoResponse toDto(RotaRequalificacao e) {
        if (e == null) return null;

        RotaRequalificacaoResponse dto = RotaRequalificacaoResponse.builder()
                .id(e.getId())
                .nomeRota(e.getNomeRota())
                .descricao(e.getDescricao())
                .objetivoProfissional(e.getObjetivoProfissional())
                .build();

        if (e.getTendencia() != null) {
            dto.setIdTendencia(e.getTendencia().getId());
        }

        if (e.getCursos() != null) {
            var steps = e.getCursos().stream()
                    .sorted(Comparator.comparing(RotaCurso::getOrdem, Comparator.nullsLast(Integer::compareTo)))
                    .map(rc -> RotaRequalificacaoResponse.RotaCursoStep.builder()
                            .idCurso(rc.getCurso() != null ? rc.getCurso().getId() : null)
                            .ordem(rc.getOrdem())
                            .tituloCurso(rc.getCurso() != null ? rc.getCurso().getTitulo() : null)
                            .build())
                    .toList();
            dto.setTrilha(steps);
        }
        return dto;
    }

    public static List<RotaRequalificacaoResponse> toDtoList(List<RotaRequalificacao> list) {
        return list == null ? List.of() : list.stream()
                .map(RotaRequalificacaoMapper::toDto)
                .collect(Collectors.toList());
    }

    // -------- CreateReq -> Entidade --------
    public static RotaRequalificacao toEntity(CreateRotaRequalificacaoRequest r) {
        if (r == null) return null;
        RotaRequalificacao e = new RotaRequalificacao();
        e.setNomeRota(r.getNomeRota());
        e.setDescricao(r.getDescricao());
        e.setObjetivoProfissional(r.getObjetivoProfissional());
        // tendencia + trilha (cursos/ordem) resolvidos no service
        return e;
    }

    // -------- UpdateReq -> Entidade (patch) --------
    public static void applyUpdate(UpdateRotaRequalificacaoRequest r, RotaRequalificacao e) {
        if (r == null || e == null) return;
        if (r.getNomeRota() != null) e.setNomeRota(r.getNomeRota());
        if (r.getDescricao() != null) e.setDescricao(r.getDescricao());
        if (r.getObjetivoProfissional() != null) e.setObjetivoProfissional(r.getObjetivoProfissional());
        // tendencia + trilha tratados no service
    }
}
