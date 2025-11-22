package br.com.fiap.mentorai.service;

import br.com.fiap.mentorai.dto.CategoriaCursoDto;
import br.com.fiap.mentorai.dto.request.create.CreateCategoriaCursoRequest;
import br.com.fiap.mentorai.dto.request.update.UpdateCategoriaCursoRequest;
import br.com.fiap.mentorai.exception.ResourceNotFoundException;
import br.com.fiap.mentorai.mapper.CategoriaCursoMapper;
import br.com.fiap.mentorai.model.CategoriaCurso;
import br.com.fiap.mentorai.repository.CategoriaCursoRepository;
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
public class CategoriaCursoService {

    private final CategoriaCursoRepository repo;

    public CategoriaCursoService(CategoriaCursoRepository repo) {
        this.repo = repo;
    }

    @Transactional
    @Caching(
            put = { @CachePut(cacheNames = "categoriasCursoById", key = "#result.id") },
            evict = { @CacheEvict(cacheNames = "categoriasCursoList", allEntries = true) }
    )
    public CategoriaCursoDto create(CreateCategoriaCursoRequest req) {
        CategoriaCurso e = CategoriaCursoMapper.toEntity(req);
        e = repo.save(e);
        return CategoriaCursoMapper.toDto(e);
    }

    @Cacheable(cacheNames = "categoriasCursoById", key = "#id")
    public CategoriaCursoDto get(UUID id) {
        CategoriaCurso e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria de curso não encontrada"));
        return CategoriaCursoMapper.toDto(e);
    }

    @Cacheable(cacheNames = "categoriasCursoList")
    public Page<CategoriaCursoDto> findAll(Pageable pageable) {
        return repo.findAll(pageable).map(CategoriaCursoMapper::toDto);
    }

    @Transactional
    @Caching(
            put = { @CachePut(cacheNames = "categoriasCursoById", key = "#result.id") },
            evict = { @CacheEvict(cacheNames = "categoriasCursoList", allEntries = true) }
    )
    public CategoriaCursoDto update(UUID id, UpdateCategoriaCursoRequest req) {
        CategoriaCurso e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria de curso não encontrada"));
        CategoriaCursoMapper.applyUpdate(req, e);
        return CategoriaCursoMapper.toDto(e);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "categoriasCursoById", key = "#id"),
            @CacheEvict(cacheNames = "categoriasCursoList", allEntries = true)
    })
    public void delete(UUID id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Categoria de curso não encontrada");
        repo.deleteById(id);
    }
}