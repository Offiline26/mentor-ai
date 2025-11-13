package br.com.fiap.mentorai.service;

import br.com.fiap.mentorai.dto.request.CreateRotaRequalificacaoRequest;
import br.com.fiap.mentorai.dto.request.UpdateRotaRequalificacaoRequest;
import br.com.fiap.mentorai.dto.response.RotaRequalificacaoResponse;
import br.com.fiap.mentorai.exception.ResourceNotFoundException;
import br.com.fiap.mentorai.mapper.RotaRequalificacaoMapper;
import br.com.fiap.mentorai.model.*;
import br.com.fiap.mentorai.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RotaRequalificacaoService {

    private final RotaRequalificacaoRepository rotaRepo;
    private final TendenciaMercadoRepository tendenciaRepo;
    private final CursoRepository cursoRepo;
    private final RotaCursoRepository rotaCursoRepo;

    public RotaRequalificacaoService(RotaRequalificacaoRepository rotaRepo,
                                     TendenciaMercadoRepository tendenciaRepo,
                                     CursoRepository cursoRepo,
                                     RotaCursoRepository rotaCursoRepo) {
        this.rotaRepo = rotaRepo;
        this.tendenciaRepo = tendenciaRepo;
        this.cursoRepo = cursoRepo;
        this.rotaCursoRepo = rotaCursoRepo;
    }

    @Transactional
    @Caching(
            put = { @CachePut(cacheNames = "rotasById", key = "#result.id") },
            evict = { @CacheEvict(cacheNames = "rotasList", allEntries = true) }
    )
    public RotaRequalificacaoResponse create(CreateRotaRequalificacaoRequest req) {
        RotaRequalificacao e = RotaRequalificacaoMapper.toEntity(req);

        if (req.getIdTendencia() != null) {
            e.setTendencia(tendenciaRepo.findById(req.getIdTendencia())
                    .orElseThrow(() -> new ResourceNotFoundException("Tendência não encontrada")));
        }

        e = rotaRepo.save(e);

        if (req.getTrilha() != null) {
            for (CreateRotaRequalificacaoRequest.RotaCursoItem item : req.getTrilha()) {
                Curso c = cursoRepo.findById(item.getIdCurso())
                        .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado: " + item.getIdCurso()));
                RotaCurso rc = RotaCurso.builder()
                        .rota(e)
                        .curso(c)
                        .ordem(item.getOrdem())
                        .build();
                rotaCursoRepo.save(rc);
                e.getCursos().add(rc);
            }
        }
        return RotaRequalificacaoMapper.toDto(e);
    }

    @Cacheable(cacheNames = "rotasById", key = "#id")
    public RotaRequalificacaoResponse get(Long id) {
        RotaRequalificacao e = rotaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rota não encontrada"));
        return RotaRequalificacaoMapper.toDto(e);
    }

    @Cacheable(cacheNames = "rotasList")
    public List<RotaRequalificacaoResponse> list() {
        return rotaRepo.findAll().stream()
                .map(RotaRequalificacaoMapper::toDto)
                .toList();
    }

    @Transactional
    @Caching(
            put = { @CachePut(cacheNames = "rotasById", key = "#result.id") },
            evict = { @CacheEvict(cacheNames = "rotasList", allEntries = true) }
    )
    public RotaRequalificacaoResponse update(Long id, UpdateRotaRequalificacaoRequest req) {
        RotaRequalificacao e = rotaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rota não encontrada"));

        RotaRequalificacaoMapper.applyUpdate(req, e);

        if (req.getIdTendencia() != null) {
            e.setTendencia(tendenciaRepo.findById(req.getIdTendencia())
                    .orElseThrow(() -> new ResourceNotFoundException("Tendência não encontrada")));
        }

        if (req.getTrilha() != null) {
            rotaCursoRepo.deleteAll(e.getCursos());
            e.getCursos().clear();

            for (CreateRotaRequalificacaoRequest.RotaCursoItem item : req.getTrilha()) {
                Curso c = cursoRepo.findById(item.getIdCurso())
                        .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado: " + item.getIdCurso()));
                RotaCurso rc = RotaCurso.builder()
                        .rota(e)
                        .curso(c)
                        .ordem(item.getOrdem())
                        .build();
                rotaCursoRepo.save(rc);
                e.getCursos().add(rc);
            }
        }

        return RotaRequalificacaoMapper.toDto(e);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "rotasById", key = "#id"),
            @CacheEvict(cacheNames = "rotasList", allEntries = true)
    })
    public void delete(Long id) {
        if (!rotaRepo.existsById(id)) throw new ResourceNotFoundException("Rota não encontrada");
        rotaRepo.deleteById(id);
    }
}
