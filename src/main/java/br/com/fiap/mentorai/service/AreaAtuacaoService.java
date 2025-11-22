package br.com.fiap.mentorai.service;

import br.com.fiap.mentorai.dto.AreaAtuacaoDto;
import br.com.fiap.mentorai.dto.request.create.CreateAreaAtuacaoRequest;
import br.com.fiap.mentorai.dto.request.update.UpdateAreaAtuacaoRequest;
import br.com.fiap.mentorai.exception.ResourceNotFoundException;
import br.com.fiap.mentorai.mapper.AreaAtuacaoMapper;
import br.com.fiap.mentorai.model.AreaAtuacao;
import br.com.fiap.mentorai.repository.AreaAtuacaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AreaAtuacaoService {

    private final AreaAtuacaoRepository repo;

    public AreaAtuacaoService(AreaAtuacaoRepository repo) {
        this.repo = repo;
    }

    @Transactional
    @Caching(
            put = { @CachePut(cacheNames = "areasById", key = "#result.id") },
            evict = { @CacheEvict(cacheNames = "areasList", allEntries = true) }
    )
    public AreaAtuacaoDto create(CreateAreaAtuacaoRequest req) {
        AreaAtuacao e = AreaAtuacaoMapper.toEntity(req);
        e = repo.save(e);
        return AreaAtuacaoMapper.toDto(e);
    }

    @Cacheable(cacheNames = "areasById", key = "#id")
    public AreaAtuacaoDto get(UUID id) {
        AreaAtuacao e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Área de atuação não encontrada"));
        return AreaAtuacaoMapper.toDto(e);
    }

    @Cacheable(cacheNames = "areasList")
    public Page<AreaAtuacaoDto> findAll(Pageable pageable) {
        return repo.findAll(pageable).map(AreaAtuacaoMapper::toDto);
    }

    @Transactional
    @Caching(
            put = { @CachePut(cacheNames = "areasById", key = "#result.id") },
            evict = { @CacheEvict(cacheNames = "areasList", allEntries = true) }
    )
    public AreaAtuacaoDto update(UUID id, UpdateAreaAtuacaoRequest req) {
        AreaAtuacao e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Área de atuação não encontrada"));
        AreaAtuacaoMapper.applyUpdate(req, e);
        return AreaAtuacaoMapper.toDto(e);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "areasById", key = "#id"),
            @CacheEvict(cacheNames = "areasList", allEntries = true)
    })
    public void delete(UUID id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Área de atuação não encontrada");
        repo.deleteById(id);
    }
}