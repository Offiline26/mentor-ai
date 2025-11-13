package br.com.fiap.mentorai.service;

import br.com.fiap.mentorai.dto.request.CreateHabilidadeRequest;
import br.com.fiap.mentorai.dto.request.UpdateHabilidadeRequest;
import br.com.fiap.mentorai.dto.response.HabilidadeResponse;
import br.com.fiap.mentorai.exception.ResourceNotFoundException;
import br.com.fiap.mentorai.mapper.HabilidadeMapper;
import br.com.fiap.mentorai.model.CategoriaHabilidade;
import br.com.fiap.mentorai.model.Habilidade;
import br.com.fiap.mentorai.repository.CategoriaHabilidadeRepository;
import br.com.fiap.mentorai.repository.HabilidadeRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabilidadeService {

    private final HabilidadeRepository repo;
    private final CategoriaHabilidadeRepository categoriaRepo;

    public HabilidadeService(HabilidadeRepository repo, CategoriaHabilidadeRepository categoriaRepo) {
        this.repo = repo;
        this.categoriaRepo = categoriaRepo;
    }

    @Transactional
    @Caching(
            put = { @CachePut(cacheNames = "habilidadesById", key = "#result.id") },
            evict = { @CacheEvict(cacheNames = "habilidadesList", allEntries = true) }
    )
    public HabilidadeResponse create(CreateHabilidadeRequest req) {
        Habilidade e = HabilidadeMapper.toEntity(req);
        if (req.getIdCategoria() != null) {
            CategoriaHabilidade cat = categoriaRepo.findById(req.getIdCategoria())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria da habilidade não encontrada"));
            e.setCategoria(cat);
        }
        e = repo.save(e);
        return HabilidadeMapper.toDto(e);
    }

    @Cacheable(cacheNames = "habilidadesById", key = "#id")
    public HabilidadeResponse get(Long id) {
        Habilidade e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Habilidade não encontrada"));
        return HabilidadeMapper.toDto(e);
    }

    @Cacheable(cacheNames = "habilidadesList")
    public List<HabilidadeResponse> list() {
        return HabilidadeMapper.toDtoList(repo.findAll());
    }

    @Transactional
    @Caching(
            put = { @CachePut(cacheNames = "habilidadesById", key = "#result.id") },
            evict = { @CacheEvict(cacheNames = "habilidadesList", allEntries = true) }
    )
    public HabilidadeResponse update(Long id, UpdateHabilidadeRequest req) {
        Habilidade e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Habilidade não encontrada"));
        HabilidadeMapper.applyUpdate(req, e);
        if (req.getIdCategoria() != null) {
            CategoriaHabilidade cat = categoriaRepo.findById(req.getIdCategoria())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria da habilidade não encontrada"));
            e.setCategoria(cat);
        }
        return HabilidadeMapper.toDto(e);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "habilidadesById", key = "#id"),
            @CacheEvict(cacheNames = "habilidadesList", allEntries = true)
    })
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Habilidade não encontrada");
        repo.deleteById(id);
    }
}