package br.com.fiap.mentorai.mapper;

import br.com.fiap.mentorai.dto.CargoDto;
import br.com.fiap.mentorai.dto.request.create.CreateCargoRequest;
import br.com.fiap.mentorai.dto.request.update.UpdateCargoRequest;
import br.com.fiap.mentorai.model.Cargo;

import java.util.List;
import java.util.stream.Collectors;

public final class CargoMapper {

    private CargoMapper() {}

    public static CargoDto toDto(Cargo e) {
        if (e == null) return null;
        return CargoDto.builder()
                .id(e.getId())
                .nome(e.getNome())
                .descricao(e.getDescricao())
                .build();
    }

    public static List<CargoDto> toDtoList(List<Cargo> list) {
        return list == null ? List.of() : list.stream()
                .map(CargoMapper::toDto)
                .collect(Collectors.toList());
    }

    public static Cargo toEntity(CreateCargoRequest r) {
        if (r == null) return null;
        Cargo e = new Cargo();
        e.setNome(r.getNome());
        e.setDescricao(r.getDescricao());
        return e;
    }

    public static void applyUpdate(UpdateCargoRequest r, Cargo e) {
        if (r == null || e == null) return;
        if (r.getNome() != null) e.setNome(r.getNome());
        if (r.getDescricao() != null) e.setDescricao(r.getDescricao());
    }
}
