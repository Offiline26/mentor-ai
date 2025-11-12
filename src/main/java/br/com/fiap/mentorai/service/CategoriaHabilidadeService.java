package br.com.fiap.mentorai.service;

import br.com.fiap.mentorai.dto.CategoriaHabilidadeDto;
import br.com.fiap.mentorai.dto.request.CreateCategoriaHabilidadeRequest;
import br.com.fiap.mentorai.dto.request.UpdateCategoriaHabilidadeRequest;
import br.com.fiap.mentorai.exception.ResourceNotFoundException;
import br.com.fiap.mentorai.mapper.CategoriaHabilidadeMapper;
import br.com.fiap.mentorai.model.CategoriaHabilidade;
import br.com.fiap.mentorai.repository.CategoriaHabilidadeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaHabilidadeService {

    private final CategoriaHabilidadeRepository repo;

    public CategoriaHabilidadeService(CategoriaHabilidadeRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public CategoriaHabilidadeDto create(CreateCategoriaHabilidadeRequest req) {
        CategoriaHabilidade e = CategoriaHabilidadeMapper.toEntity(req);
        e = repo.save(e);
        return CategoriaHabilidadeMapper.toDto(e);
    }

    public CategoriaHabilidadeDto get(Long id) {
        CategoriaHabilidade e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria de habilidade não encontrada"));
        return CategoriaHabilidadeMapper.toDto(e);
    }

    public List<CategoriaHabilidadeDto> list() {
        return CategoriaHabilidadeMapper.toDtoList(repo.findAll());
    }

    @Transactional
    public CategoriaHabilidadeDto update(Long id, UpdateCategoriaHabilidadeRequest req) {
        CategoriaHabilidade e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria de habilidade não encontrada"));
        CategoriaHabilidadeMapper.applyUpdate(req, e);
        return CategoriaHabilidadeMapper.toDto(e);
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Categoria de habilidade não encontrada");
        repo.deleteById(id);
    }
}