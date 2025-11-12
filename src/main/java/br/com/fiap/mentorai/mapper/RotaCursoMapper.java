package br.com.fiap.mentorai.mapper;


import br.com.fiap.mentorai.dto.response.RotaCursoResponse;
import br.com.fiap.mentorai.model.RotaCurso;

import java.util.List;
import java.util.stream.Collectors;

public final class RotaCursoMapper {

    private RotaCursoMapper() {}

    public static RotaCursoResponse toDto(RotaCurso e) {
        if (e == null) return null;
        return RotaCursoResponse.builder()
                .idRota(e.getRota() != null ? e.getRota().getId() : null)
                .idCurso(e.getCurso() != null ? e.getCurso().getId() : null)
                .ordem(e.getOrdem())
                .tituloCurso(e.getCurso() != null ? e.getCurso().getTitulo() : null)
                .build();
    }

    public static List<RotaCursoResponse> toDtoList(List<RotaCurso> list) {
        return list == null ? List.of() : list.stream()
                .map(RotaCursoMapper::toDto)
                .collect(Collectors.toList());
    }
}
