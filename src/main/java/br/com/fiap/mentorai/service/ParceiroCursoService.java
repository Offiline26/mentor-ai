package br.com.fiap.mentorai.service;

import br.com.fiap.mentorai.dto.ParceiroCursoDto;
import br.com.fiap.mentorai.dto.request.CreateParceiroCursoRequest;
import br.com.fiap.mentorai.dto.request.UpdateParceiroCursoRequest;
import br.com.fiap.mentorai.exception.ResourceNotFoundException;
import br.com.fiap.mentorai.mapper.ParceiroCursoMapper;
import br.com.fiap.mentorai.model.ParceiroCurso;
import br.com.fiap.mentorai.repository.ParceiroCursoRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParceiroCursoService {

    private final ParceiroCursoRepository repo;

    public ParceiroCursoService(ParceiroCursoRepository repo) {
        this.repo = repo;
    }

    @Transactional
    @Caching(
            put = { @CachePut(cacheNames = "parceirosById", key = "#result.id") },
            evict = { @CacheEvict(cacheNames = "parceirosList", allEntries = true) }
    )
    public ParceiroCursoDto create(CreateParceiroCursoRequest req) {
        ParceiroCurso e = ParceiroCursoMapper.toEntity(req);
        e = repo.save(e);
        return ParceiroCursoMapper.toDto(e);
    }

    @Cacheable(cacheNames = "parceirosById", key = "#id")
    public ParceiroCursoDto get(Long id) {
        ParceiroCurso e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parceiro de curso não encontrado"));
        return ParceiroCursoMapper.toDto(e);
    }

    @Cacheable(cacheNames = "parceirosList")
    public List<ParceiroCursoDto> list() {
        return ParceiroCursoMapper.toDtoList(repo.findAll());
    }

    @Transactional
    @Caching(
            put = { @CachePut(cacheNames = "parceirosById", key = "#result.id") },
            evict = { @CacheEvict(cacheNames = "parceirosList", allEntries = true) }
    )
    public ParceiroCursoDto update(Long id, UpdateParceiroCursoRequest req) {
        ParceiroCurso e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parceiro de curso não encontrado"));
        ParceiroCursoMapper.applyUpdate(req, e);
        return ParceiroCursoMapper.toDto(e);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "parceirosById", key = "#id"),
            @CacheEvict(cacheNames = "parceirosList", allEntries = true)
    })
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Parceiro de curso não encontrado");
        repo.deleteById(id);
    }
}
