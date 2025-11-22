package br.com.fiap.mentorai.service;

import br.com.fiap.mentorai.dto.CategoriaHabilidadeDto;
import br.com.fiap.mentorai.dto.request.create.CreateCategoriaHabilidadeRequest;
import br.com.fiap.mentorai.dto.request.update.UpdateCategoriaHabilidadeRequest;
import br.com.fiap.mentorai.exception.ResourceNotFoundException;
import br.com.fiap.mentorai.mapper.CategoriaHabilidadeMapper;
import br.com.fiap.mentorai.model.CategoriaHabilidade;
import br.com.fiap.mentorai.repository.CategoriaHabilidadeRepository;
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
public class CategoriaHabilidadeService {

    private final CategoriaHabilidadeRepository repo;

    public CategoriaHabilidadeService(CategoriaHabilidadeRepository repo) {
        this.repo = repo;
    }

    @Transactional
    @Caching(
            put = { @CachePut(cacheNames = "categoriasHabById", key = "#result.id") },
            evict = { @CacheEvict(cacheNames = "categoriasHabList", allEntries = true) }
    )
    public CategoriaHabilidadeDto create(CreateCategoriaHabilidadeRequest req) {
        CategoriaHabilidade e = CategoriaHabilidadeMapper.toEntity(req);
        e = repo.save(e);
        return CategoriaHabilidadeMapper.toDto(e);
    }

    @Cacheable(cacheNames = "categoriasHabById", key = "#id")
    public CategoriaHabilidadeDto get(UUID id) {
        CategoriaHabilidade e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria de habilidade não encontrada"));
        return CategoriaHabilidadeMapper.toDto(e);
    }

    @Cacheable(cacheNames = "categoriasHabList")
    public Page<CategoriaHabilidadeDto> findAll(Pageable pageable) {
        return repo.findAll(pageable).map(CategoriaHabilidadeMapper::toDto);
    }

    @Transactional
    @Caching(
            put = { @CachePut(cacheNames = "categoriasHabById", key = "#result.id") },
            evict = { @CacheEvict(cacheNames = "categoriasHabList", allEntries = true) }
    )
    public CategoriaHabilidadeDto update(UUID id, UpdateCategoriaHabilidadeRequest req) {
        CategoriaHabilidade e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria de habilidade não encontrada"));
        CategoriaHabilidadeMapper.applyUpdate(req, e);
        return CategoriaHabilidadeMapper.toDto(e);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "categoriasHabById", key = "#id"),
            @CacheEvict(cacheNames = "categoriasHabList", allEntries = true)
    })
    public void delete(UUID id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Categoria de habilidade não encontrada");
        repo.deleteById(id);
    }
}