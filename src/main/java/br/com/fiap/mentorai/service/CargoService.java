package br.com.fiap.mentorai.service;

import br.com.fiap.mentorai.dto.CargoDto;
import br.com.fiap.mentorai.dto.request.CreateCargoRequest;
import br.com.fiap.mentorai.dto.request.UpdateCargoRequest;
import br.com.fiap.mentorai.exception.ResourceNotFoundException;
import br.com.fiap.mentorai.mapper.CargoMapper;
import br.com.fiap.mentorai.model.Cargo;
import br.com.fiap.mentorai.repository.CargoRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CargoService {

    private final CargoRepository repo;

    public CargoService(CargoRepository repo) {
        this.repo = repo;
    }

    @Transactional
    @Caching(
            put = { @CachePut(cacheNames = "cargosById", key = "#result.id") },
            evict = { @CacheEvict(cacheNames = "cargosList", allEntries = true) }
    )
    public CargoDto create(CreateCargoRequest req) {
        Cargo e = CargoMapper.toEntity(req);
        e = repo.save(e);
        return CargoMapper.toDto(e);
    }

    @Cacheable(cacheNames = "cargosById", key = "#id")
    public CargoDto get(Long id) {
        Cargo e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado"));
        return CargoMapper.toDto(e);
    }

    @Cacheable(cacheNames = "cargosList")
    public List<CargoDto> list() {
        return CargoMapper.toDtoList(repo.findAll());
    }

    @Transactional
    @Caching(
            put = { @CachePut(cacheNames = "cargosById", key = "#result.id") },
            evict = { @CacheEvict(cacheNames = "cargosList", allEntries = true) }
    )
    public CargoDto update(Long id, UpdateCargoRequest req) {
        Cargo e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado"));
        CargoMapper.applyUpdate(req, e);
        return CargoMapper.toDto(e);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "cargosById", key = "#id"),
            @CacheEvict(cacheNames = "cargosList", allEntries = true)
    })
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Cargo não encontrado");
        repo.deleteById(id);
    }
}

