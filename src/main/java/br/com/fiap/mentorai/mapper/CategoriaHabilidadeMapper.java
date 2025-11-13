package br.com.fiap.mentorai.mapper;

import br.com.fiap.mentorai.dto.CategoriaHabilidadeDto;
import br.com.fiap.mentorai.dto.request.create.CreateCategoriaHabilidadeRequest;
import br.com.fiap.mentorai.dto.request.update.UpdateCategoriaHabilidadeRequest;
import br.com.fiap.mentorai.model.CategoriaHabilidade;

import java.util.List;
import java.util.stream.Collectors;

public final class CategoriaHabilidadeMapper {

    private CategoriaHabilidadeMapper() {}

    public static CategoriaHabilidadeDto toDto(CategoriaHabilidade e) {
        if (e == null) return null;
        return CategoriaHabilidadeDto.builder()
                .id(e.getId())
                .nome(e.getNome())
                .descricao(e.getDescricao())
                .build();
    }

    public static List<CategoriaHabilidadeDto> toDtoList(List<CategoriaHabilidade> list) {
        return list == null ? List.of() : list.stream()
                .map(CategoriaHabilidadeMapper::toDto)
                .collect(Collectors.toList());
    }

    public static CategoriaHabilidade toEntity(CreateCategoriaHabilidadeRequest r) {
        if (r == null) return null;
        CategoriaHabilidade e = new CategoriaHabilidade();
        e.setNome(r.getNome());
        e.setDescricao(r.getDescricao());
        return e;
    }

    public static void applyUpdate(UpdateCategoriaHabilidadeRequest r, CategoriaHabilidade e) {
        if (r == null || e == null) return;
        if (r.getNome() != null) e.setNome(r.getNome());
        if (r.getDescricao() != null) e.setDescricao(r.getDescricao());
    }
}
