package br.com.fiap.mentorai.mapper;

import br.com.fiap.mentorai.dto.request.create.CreateHabilidadeRequest;
import br.com.fiap.mentorai.dto.request.update.UpdateHabilidadeRequest;
import br.com.fiap.mentorai.dto.response.HabilidadeResponse;
import br.com.fiap.mentorai.model.Habilidade;

import java.util.List;
import java.util.stream.Collectors;

public final class HabilidadeMapper {

    private HabilidadeMapper() {}

    public static HabilidadeResponse toDto(Habilidade e) {
        if (e == null) return null;
        HabilidadeResponse dto = HabilidadeResponse.builder()
                .id(e.getId())
                .nome(e.getNome())
                .descricao(e.getDescricao())
                .build();
        if (e.getCategoria() != null) {
            dto.setIdCategoria(e.getCategoria().getId());
            dto.setCategoria(e.getCategoria().getNome());
        }
        return dto;
    }

    public static List<HabilidadeResponse> toDtoList(List<Habilidade> list) {
        return list == null ? List.of() : list.stream()
                .map(HabilidadeMapper::toDto)
                .collect(Collectors.toList());
    }

    public static Habilidade toEntity(CreateHabilidadeRequest r) {
        if (r == null) return null;
        Habilidade e = new Habilidade();
        e.setNome(r.getNome());
        e.setDescricao(r.getDescricao());
        // categoria por ID será resolvida no service
        return e;
    }

    public static void applyUpdate(UpdateHabilidadeRequest r, Habilidade e) {
        if (r == null || e == null) return;
        if (r.getNome() != null) e.setNome(r.getNome());
        if (r.getDescricao() != null) e.setDescricao(r.getDescricao());
        // categoria por ID no service
    }
}
