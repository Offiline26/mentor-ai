package br.com.fiap.mentorai.service;

import br.com.fiap.mentorai.dto.CategoriaCursoDto;
import br.com.fiap.mentorai.dto.request.CreateCategoriaCursoRequest;
import br.com.fiap.mentorai.dto.request.UpdateCategoriaCursoRequest;
import br.com.fiap.mentorai.exception.ResourceNotFoundException;
import br.com.fiap.mentorai.mapper.CategoriaCursoMapper;
import br.com.fiap.mentorai.model.CategoriaCurso;
import br.com.fiap.mentorai.repository.CategoriaCursoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaCursoService {

    private final CategoriaCursoRepository repo;

    public CategoriaCursoService(CategoriaCursoRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public CategoriaCursoDto create(CreateCategoriaCursoRequest req) {
        CategoriaCurso e = CategoriaCursoMapper.toEntity(req);
        e = repo.save(e);
        return CategoriaCursoMapper.toDto(e);
    }

    public CategoriaCursoDto get(Long id) {
        CategoriaCurso e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria de curso não encontrada"));
        return CategoriaCursoMapper.toDto(e);
    }

    public List<CategoriaCursoDto> list() {
        return CategoriaCursoMapper.toDtoList(repo.findAll());
    }

    @Transactional
    public CategoriaCursoDto update(Long id, UpdateCategoriaCursoRequest req) {
        CategoriaCurso e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria de curso não encontrada"));
        CategoriaCursoMapper.applyUpdate(req, e);
        return CategoriaCursoMapper.toDto(e);
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Categoria de curso não encontrada");
        repo.deleteById(id);
    }
}