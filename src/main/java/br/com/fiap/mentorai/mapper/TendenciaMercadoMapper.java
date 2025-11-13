package br.com.fiap.mentorai.mapper;

import br.com.fiap.mentorai.dto.request.create.CreateTendenciaMercadoRequest;
import br.com.fiap.mentorai.dto.request.update.UpdateTendenciaMercadoRequest;
import br.com.fiap.mentorai.dto.response.TendenciaMercadoResponse;
import br.com.fiap.mentorai.model.TendenciaMercado;
import br.com.fiap.mentorai.model.enums.NivelDemandaEnum;

import java.util.List;
import java.util.stream.Collectors;

public final class TendenciaMercadoMapper {

    private TendenciaMercadoMapper() {}

    public static TendenciaMercadoResponse toDto(TendenciaMercado e) {
        if (e == null) return null;
        return TendenciaMercadoResponse.builder()
                .id(e.getId())
                .descricao(e.getDescricao())
                .indiceDemanda(e.getIndiceDemanda())
                .fonte(e.getFonte())
                .dataAnalise(e.getDataAnalise())
                .nivelDemanda(calcNivel(e)) // conveniência
                .build();
    }

    public static List<TendenciaMercadoResponse> toDtoList(List<TendenciaMercado> list) {
        return list == null ? List.of() : list.stream()
                .map(TendenciaMercadoMapper::toDto)
                .collect(Collectors.toList());
    }

    public static TendenciaMercado toEntity(CreateTendenciaMercadoRequest r) {
        if (r == null) return null;
        TendenciaMercado e = new TendenciaMercado();
        e.setDescricao(r.getDescricao());
        e.setIndiceDemanda(r.getIndiceDemanda());
        e.setFonte(r.getFonte());
        e.setDataAnalise(r.getDataAnalise());
        return e;
    }

    public static void applyUpdate(UpdateTendenciaMercadoRequest r, TendenciaMercado e) {
        if (r == null || e == null) return;
        if (r.getDescricao() != null) e.setDescricao(r.getDescricao());
        if (r.getIndiceDemanda() != null) e.setIndiceDemanda(r.getIndiceDemanda());
        if (r.getFonte() != null) e.setFonte(r.getFonte());
        if (r.getDataAnalise() != null) e.setDataAnalise(r.getDataAnalise());
    }

    private static NivelDemandaEnum calcNivel(TendenciaMercado e) {
        if (e.getIndiceDemanda() == null) return null;
        double v = e.getIndiceDemanda().doubleValue();
        if (v < 25.0) return NivelDemandaEnum.BAIXA;
        if (v < 50.0) return NivelDemandaEnum.MEDIA;
        if (v < 75.0) return NivelDemandaEnum.ALTA;
        return NivelDemandaEnum.MUITO_ALTA;
    }
}
