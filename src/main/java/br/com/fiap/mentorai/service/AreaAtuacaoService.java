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
import org.springframework.stereotype.Service;

import java.util.List;

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
    public AreaAtuacaoDto get(Long id) {
        AreaAtuacao e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Área de atuação não encontrada"));
        return AreaAtuacaoMapper.toDto(e);
    }

    @Cacheable(cacheNames = "areasList")
    public List<AreaAtuacaoDto> list() {
        return AreaAtuacaoMapper.toDtoList(repo.findAll());
    }

    @Transactional
    @Caching(
            put = { @CachePut(cacheNames = "areasById", key = "#result.id") },
            evict = { @CacheEvict(cacheNames = "areasList", allEntries = true) }
    )
    public AreaAtuacaoDto update(Long id, UpdateAreaAtuacaoRequest req) {
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
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Área de atuação não encontrada");
        repo.deleteById(id);
    }
}