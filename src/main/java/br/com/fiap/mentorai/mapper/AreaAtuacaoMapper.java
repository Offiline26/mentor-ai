package br.com.fiap.mentorai.mapper;

import br.com.fiap.mentorai.dto.AreaAtuacaoDto;
import br.com.fiap.mentorai.dto.request.CreateAreaAtuacaoRequest;
import br.com.fiap.mentorai.dto.request.UpdateAreaAtuacaoRequest;
import br.com.fiap.mentorai.model.AreaAtuacao;

import java.util.List;
import java.util.stream.Collectors;

public final class AreaAtuacaoMapper {

    private AreaAtuacaoMapper() {}

    public static AreaAtuacaoDto toDto(AreaAtuacao e) {
        if (e == null) return null;
        return AreaAtuacaoDto.builder()
                .id(e.getId())
                .nome(e.getNome())
                .descricao(e.getDescricao())
                .build();
    }

    public static List<AreaAtuacaoDto> toDtoList(List<AreaAtuacao> list) {
        return list == null ? List.of() : list.stream()
                .map(AreaAtuacaoMapper::toDto)
                .collect(Collectors.toList());
    }

    public static AreaAtuacao toEntity(CreateAreaAtuacaoRequest r) {
        if (r == null) return null;
        AreaAtuacao e = new AreaAtuacao();
        e.setNome(r.getNome());
        e.setDescricao(r.getDescricao());
        return e;
    }

    public static void applyUpdate(UpdateAreaAtuacaoRequest r, AreaAtuacao e) {
        if (r == null || e == null) return;
        if (r.getNome() != null) e.setNome(r.getNome());
        if (r.getDescricao() != null) e.setDescricao(r.getDescricao());
    }
}
