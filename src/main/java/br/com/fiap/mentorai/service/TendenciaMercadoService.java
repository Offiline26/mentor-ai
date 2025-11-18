package br.com.fiap.mentorai.service;

import br.com.fiap.mentorai.dto.request.create.CreateTendenciaMercadoRequest;
import br.com.fiap.mentorai.dto.request.update.UpdateTendenciaMercadoRequest;
import br.com.fiap.mentorai.dto.response.TendenciaMercadoResponse;
import br.com.fiap.mentorai.exception.ResourceNotFoundException;
import br.com.fiap.mentorai.mapper.TendenciaMercadoMapper;
import br.com.fiap.mentorai.model.TendenciaMercado;
import br.com.fiap.mentorai.repository.TendenciaMercadoRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TendenciaMercadoService {

    private final TendenciaMercadoRepository repo;

    public TendenciaMercadoService(TendenciaMercadoRepository repo) {
        this.repo = repo;
    }

    @Transactional
    @Caching(
            put = { @CachePut(cacheNames = "tendenciasById", key = "#result.id") },
            evict = { @CacheEvict(cacheNames = "tendenciasList", allEntries = true) }
    )
    public TendenciaMercadoResponse create(CreateTendenciaMercadoRequest req) {
        TendenciaMercado e = TendenciaMercadoMapper.toEntity(req);
        e = repo.save(e);
        return TendenciaMercadoMapper.toDto(e);
    }

    @Cacheable(cacheNames = "tendenciasById", key = "#id")
    public TendenciaMercadoResponse get(UUID id) {
        TendenciaMercado e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tendência não encontrada"));
        return TendenciaMercadoMapper.toDto(e);
    }

    @Cacheable(cacheNames = "tendenciasList")
    public List<TendenciaMercadoResponse> list() {
        return TendenciaMercadoMapper.toDtoList(repo.findAll());
    }

    @Transactional
    @Caching(
            put = { @CachePut(cacheNames = "tendenciasById", key = "#result.id") },
            evict = { @CacheEvict(cacheNames = "tendenciasList", allEntries = true) }
    )
    public TendenciaMercadoResponse update(UUID id, UpdateTendenciaMercadoRequest req) {
        TendenciaMercado e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tendência não encontrada"));
        TendenciaMercadoMapper.applyUpdate(req, e);
        return TendenciaMercadoMapper.toDto(e);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "tendenciasById", key = "#id"),
            @CacheEvict(cacheNames = "tendenciasList", allEntries = true)
    })
    public void delete(UUID id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Tendência não encontrada");
        repo.deleteById(id);
    }
}
