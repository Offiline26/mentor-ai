package br.com.fiap.mentorai.mapper;

import br.com.fiap.mentorai.dto.response.CursoResponse;
import br.com.fiap.mentorai.dto.request.CreateCursoRequest;
import br.com.fiap.mentorai.dto.request.UpdateCursoRequest;
import br.com.fiap.mentorai.model.Curso;
import br.com.fiap.mentorai.model.Habilidade;

import java.util.List;
import java.util.stream.Collectors;

public final class CursoMapper {

    private CursoMapper() {}

    // -------- Entidade -> DTO --------
    public static CursoResponse toDto(Curso e) {
        if (e == null) return null;
        CursoResponse dto = CursoResponse.builder()
                .id(e.getId())
                .titulo(e.getTitulo())
                .descricao(e.getDescricao())
                .duracaoHoras(e.getDuracaoHoras())
                .linkCurso(e.getLinkCurso())
                .build();

        if (e.getParceiro() != null) {
            dto.setIdParceiro(e.getParceiro().getId());
            dto.setParceiro(e.getParceiro().getNome());
        }
        if (e.getCategoriaCurso() != null) {
            dto.setIdCategoriaCurso(e.getCategoriaCurso().getId());
            dto.setCategoriaCurso(e.getCategoriaCurso().getNome());
        }
        if (e.getHabilidades() != null) {
            dto.setHabilidades(e.getHabilidades().stream()
                    .map(Habilidade::getId)
                    .collect(Collectors.toSet()));
        }
        return dto;
    }

    public static List<CursoResponse> toDtoList(List<Curso> list) {
        return list == null ? List.of() : list.stream()
                .map(CursoMapper::toDto)
                .collect(Collectors.toList());
    }

    // -------- CreateReq -> Entidade --------
    public static Curso toEntity(CreateCursoRequest r) {
        if (r == null) return null;
        Curso e = new Curso();
        e.setTitulo(r.getTitulo());
        e.setDescricao(r.getDescricao());
        e.setDuracaoHoras(r.getDuracaoHoras());
        e.setLinkCurso(r.getLinkCurso());
        // parceiro, categoria e habilidades por IDs: resolver no service
        return e;
    }

    // -------- UpdateReq -> Entidade (patch) --------
    public static void applyUpdate(UpdateCursoRequest r, Curso e) {
        if (r == null || e == null) return;
        if (r.getTitulo() != null) e.setTitulo(r.getTitulo());
        if (r.getDescricao() != null) e.setDescricao(r.getDescricao());
        if (r.getDuracaoHoras() != null) e.setDuracaoHoras(r.getDuracaoHoras());
        if (r.getLinkCurso() != null) e.setLinkCurso(r.getLinkCurso());
        // parceiro, categoria e habilidades tratados no service
    }
}
