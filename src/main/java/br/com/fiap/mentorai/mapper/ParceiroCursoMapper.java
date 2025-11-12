package br.com.fiap.mentorai.mapper;

import br.com.fiap.mentorai.dto.ParceiroCursoDto;
import br.com.fiap.mentorai.dto.request.CreateParceiroCursoRequest;
import br.com.fiap.mentorai.dto.request.UpdateParceiroCursoRequest;
import br.com.fiap.mentorai.model.ParceiroCurso;

import java.util.List;
import java.util.stream.Collectors;

public final class ParceiroCursoMapper {

    private ParceiroCursoMapper() {}

    public static ParceiroCursoDto toDto(ParceiroCurso e) {
        if (e == null) return null;
        return ParceiroCursoDto.builder()
                .id(e.getId())
                .nome(e.getNome())
                .descricao(e.getDescricao())
                .build();
    }

    public static List<ParceiroCursoDto> toDtoList(List<ParceiroCurso> list) {
        return list == null ? List.of() : list.stream()
                .map(ParceiroCursoMapper::toDto)
                .collect(Collectors.toList());
    }

    public static ParceiroCurso toEntity(CreateParceiroCursoRequest r) {
        if (r == null) return null;
        ParceiroCurso e = new ParceiroCurso();
        e.setNome(r.getNome());
        e.setDescricao(r.getDescricao());
        return e;
    }

    public static void applyUpdate(UpdateParceiroCursoRequest r, ParceiroCurso e) {
        if (r == null || e == null) return;
        if (r.getNome() != null) e.setNome(r.getNome());
        if (r.getDescricao() != null) e.setDescricao(r.getDescricao());
    }
}
