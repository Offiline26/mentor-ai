package br.com.fiap.mentorai.service;

import br.com.fiap.mentorai.dto.CargoDto;
import br.com.fiap.mentorai.dto.request.CreateCargoRequest;
import br.com.fiap.mentorai.dto.request.UpdateCargoRequest;
import br.com.fiap.mentorai.exception.ResourceNotFoundException;
import br.com.fiap.mentorai.mapper.CargoMapper;
import br.com.fiap.mentorai.model.Cargo;
import br.com.fiap.mentorai.repository.CargoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CargoService {

    private final CargoRepository repo;

    public CargoService(CargoRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public CargoDto create(CreateCargoRequest req) {
        Cargo e = CargoMapper.toEntity(req);
        e = repo.save(e);
        return CargoMapper.toDto(e);
    }

    public CargoDto get(Long id) {
        Cargo e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado"));
        return CargoMapper.toDto(e);
    }

    public List<CargoDto> list() {
        return CargoMapper.toDtoList(repo.findAll());
    }

    @Transactional
    public CargoDto update(Long id, UpdateCargoRequest req) {
        Cargo e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado"));
        CargoMapper.applyUpdate(req, e);
        return CargoMapper.toDto(e);
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Cargo não encontrado");
        repo.deleteById(id);
    }
}
