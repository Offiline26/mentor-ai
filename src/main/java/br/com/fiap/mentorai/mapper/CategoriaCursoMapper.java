package br.com.fiap.mentorai.mapper;

import br.com.fiap.mentorai.dto.CategoriaCursoDto;
import br.com.fiap.mentorai.dto.request.CreateCategoriaCursoRequest;
import br.com.fiap.mentorai.dto.request.UpdateCategoriaCursoRequest;
import br.com.fiap.mentorai.model.CategoriaCurso;

import java.util.List;
import java.util.stream.Collectors;

public final class CategoriaCursoMapper {

    private CategoriaCursoMapper() {}

    public static CategoriaCursoDto toDto(CategoriaCurso e) {
        if (e == null) return null;
        return CategoriaCursoDto.builder()
                .id(e.getId())
                .nome(e.getNome())
                .descricao(e.getDescricao())
                .build();
    }

    public static List<CategoriaCursoDto> toDtoList(List<CategoriaCurso> list) {
        return list == null ? List.of() : list.stream()
                .map(CategoriaCursoMapper::toDto)
                .collect(Collectors.toList());
    }

    public static CategoriaCurso toEntity(CreateCategoriaCursoRequest r) {
        if (r == null) return null;
        CategoriaCurso e = new CategoriaCurso();
        e.setNome(r.getNome());
        e.setDescricao(r.getDescricao());
        return e;
    }

    public static void applyUpdate(UpdateCategoriaCursoRequest r, CategoriaCurso e) {
        if (r == null || e == null) return;
        if (r.getNome() != null) e.setNome(r.getNome());
        if (r.getDescricao() != null) e.setDescricao(r.getDescricao());
    }
}