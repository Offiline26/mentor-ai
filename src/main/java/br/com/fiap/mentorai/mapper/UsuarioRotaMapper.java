package br.com.fiap.mentorai.mapper;

import br.com.fiap.mentorai.dto.response.UsuarioRotaResponse;
import br.com.fiap.mentorai.model.UsuarioRota;

import java.util.List;
import java.util.stream.Collectors;

public final class UsuarioRotaMapper {

    private UsuarioRotaMapper() {}

    public static UsuarioRotaResponse toDto(UsuarioRota e) {
        if (e == null) return null;
        return UsuarioRotaResponse.builder()
                .idUsuario(e.getUsuario() != null ? e.getUsuario().getId() : null)
                .idRota(e.getRota() != null ? e.getRota().getId() : null)
                .nomeRota(e.getRota() != null ? e.getRota().getNomeRota() : null)
                .progressoPercentual(e.getProgressoPercentual())
                .status(e.getStatus() != null ? e.getStatus().name() : null)
                .dataInicio(e.getDataInicio())
                .dataConclusao(e.getDataConclusao())
                .build();
    }

    public static List<UsuarioRotaResponse> toDtoList(List<UsuarioRota> list) {
        return list == null ? List.of() : list.stream()
                .map(UsuarioRotaMapper::toDto)
                .collect(Collectors.toList());
    }
}
