package br.com.fiap.mentorai.service;

import br.com.fiap.mentorai.dto.request.CreateTendenciaMercadoRequest;
import br.com.fiap.mentorai.dto.request.UpdateTendenciaMercadoRequest;
import br.com.fiap.mentorai.dto.response.TendenciaMercadoResponse;
import br.com.fiap.mentorai.exception.ResourceNotFoundException;
import br.com.fiap.mentorai.mapper.TendenciaMercadoMapper;
import br.com.fiap.mentorai.model.TendenciaMercado;
import br.com.fiap.mentorai.repository.TendenciaMercadoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TendenciaMercadoService {

    private final TendenciaMercadoRepository repo;

    public TendenciaMercadoService(TendenciaMercadoRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public TendenciaMercadoResponse create(CreateTendenciaMercadoRequest req) {
        TendenciaMercado e = TendenciaMercadoMapper.toEntity(req);
        e = repo.save(e);
        return TendenciaMercadoMapper.toDto(e);
    }

    public TendenciaMercadoResponse get(Long id) {
        TendenciaMercado e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tendência não encontrada"));
        return TendenciaMercadoMapper.toDto(e);
    }

    public List<TendenciaMercadoResponse> list() {
        return TendenciaMercadoMapper.toDtoList(repo.findAll());
    }

    @Transactional
    public TendenciaMercadoResponse update(Long id, UpdateTendenciaMercadoRequest req) {
        TendenciaMercado e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tendência não encontrada"));
        TendenciaMercadoMapper.applyUpdate(req, e);
        return TendenciaMercadoMapper.toDto(e);
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Tendência não encontrada");
        repo.deleteById(id);
    }
}
