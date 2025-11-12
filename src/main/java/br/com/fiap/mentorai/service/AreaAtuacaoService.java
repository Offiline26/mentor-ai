package br.com.fiap.mentorai.service;

import br.com.fiap.mentorai.dto.AreaAtuacaoDto;
import br.com.fiap.mentorai.dto.request.CreateAreaAtuacaoRequest;
import br.com.fiap.mentorai.dto.request.UpdateAreaAtuacaoRequest;
import br.com.fiap.mentorai.exception.ResourceNotFoundException;
import br.com.fiap.mentorai.mapper.AreaAtuacaoMapper;
import br.com.fiap.mentorai.model.AreaAtuacao;
import br.com.fiap.mentorai.repository.AreaAtuacaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaAtuacaoService {

    private final AreaAtuacaoRepository repo;

    public AreaAtuacaoService(AreaAtuacaoRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public AreaAtuacaoDto create(CreateAreaAtuacaoRequest req) {
        AreaAtuacao e = AreaAtuacaoMapper.toEntity(req);
        e = repo.save(e);
        return AreaAtuacaoMapper.toDto(e);
    }

    public AreaAtuacaoDto get(Long id) {
        AreaAtuacao e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Área de atuação não encontrada"));
        return AreaAtuacaoMapper.toDto(e);
    }

    public List<AreaAtuacaoDto> list() {
        return AreaAtuacaoMapper.toDtoList(repo.findAll());
    }

    @Transactional
    public AreaAtuacaoDto update(Long id, UpdateAreaAtuacaoRequest req) {
        AreaAtuacao e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Área de atuação não encontrada"));
        AreaAtuacaoMapper.applyUpdate(req, e);
        return AreaAtuacaoMapper.toDto(e);
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Área de atuação não encontrada");
        repo.deleteById(id);
    }
}