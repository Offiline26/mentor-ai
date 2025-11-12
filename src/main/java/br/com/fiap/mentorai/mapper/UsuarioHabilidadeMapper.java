package br.com.fiap.mentorai.mapper;

import br.com.fiap.mentorai.dto.response.UsuarioHabilidadeResponse;
import br.com.fiap.mentorai.model.UsuarioHabilidade;

import java.util.List;
import java.util.stream.Collectors;

public final class UsuarioHabilidadeMapper {

    private UsuarioHabilidadeMapper() {}

    public static UsuarioHabilidadeResponse toDto(UsuarioHabilidade e) {
        if (e == null) return null;
        return UsuarioHabilidadeResponse.builder()
                .idUsuario(e.getUsuario() != null ? e.getUsuario().getId() : null)
                .idHabilidade(e.getHabilidade() != null ? e.getHabilidade().getId() : null)
                .habilidade(e.getHabilidade() != null ? e.getHabilidade().getNome() : null)
                .nivel(e.getNivelProficiencia() != null ? e.getNivelProficiencia().getValorDb() : null)
                .build();
    }

    public static List<UsuarioHabilidadeResponse> toDtoList(List<UsuarioHabilidade> list) {
        return list == null ? List.of() : list.stream()
                .map(UsuarioHabilidadeMapper::toDto)
                .collect(Collectors.toList());
    }
}
