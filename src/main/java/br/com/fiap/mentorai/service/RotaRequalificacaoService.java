package br.com.fiap.mentorai.service;

import br.com.fiap.mentorai.dto.request.create.CreateRotaRequalificacaoRequest;
import br.com.fiap.mentorai.dto.request.update.UpdateRotaRequalificacaoRequest;
import br.com.fiap.mentorai.dto.response.RotaRequalificacaoResponse;
import br.com.fiap.mentorai.exception.ResourceNotFoundException;
import br.com.fiap.mentorai.mapper.RotaRequalificacaoMapper;
import br.com.fiap.mentorai.messaging.RotaEventPublisher;
import br.com.fiap.mentorai.messaging.RotaCriadaEvent;
import br.com.fiap.mentorai.model.Curso;
import br.com.fiap.mentorai.model.RotaCurso;
import br.com.fiap.mentorai.model.RotaRequalificacao;
import br.com.fiap.mentorai.repository.CursoRepository;
import br.com.fiap.mentorai.repository.RotaCursoRepository;
import br.com.fiap.mentorai.repository.RotaRequalificacaoRepository;
import br.com.fiap.mentorai.repository.TendenciaMercadoRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class RotaRequalificacaoService {

    private final RotaRequalificacaoRepository rotaRepo;
    private final TendenciaMercadoRepository tendenciaRepo;
    private final CursoRepository cursoRepo;
    private final RotaCursoRepository rotaCursoRepo;
    private final RotaEventPublisher rotaEventPublisher;

    public RotaRequalificacaoService(RotaRequalificacaoRepository rotaRepo,
                                     TendenciaMercadoRepository tendenciaRepo,
                                     CursoRepository cursoRepo,
                                     RotaCursoRepository rotaCursoRepo,
                                     RotaEventPublisher rotaEventPublisher) {
        this.rotaRepo = rotaRepo;
        this.tendenciaRepo = tendenciaRepo;
        this.cursoRepo = cursoRepo;
        this.rotaCursoRepo = rotaCursoRepo;
        this.rotaEventPublisher = rotaEventPublisher;
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

        // monta DTO para retorno
        RotaRequalificacaoResponse dto = RotaRequalificacaoMapper.toDto(e);

        // publica evento assíncrono no RabbitMQ
        RotaCriadaEvent event = RotaCriadaEvent.builder()
                .idRota(e.getId())
                .nomeRota(e.getNomeRota())
                .objetivoProfissional(e.getObjetivoProfissional())
                .dataCriacao(LocalDateTime.now())
                .cursos(
                        e.getCursos().stream()
                                .map(rc -> rc.getCurso().getId())
                                .toList()
                )
                .build();

        rotaEventPublisher.publishRotaCriada(event);

        return dto;
    }

    @Cacheable(cacheNames = "rotasById", key = "#id")
    public RotaRequalificacaoResponse get(UUID id) {
        RotaRequalificacao e = rotaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rota não encontrada"));
        return RotaRequalificacaoMapper.toDto(e);
    }

    @Cacheable(cacheNames = "rotasList")
    public Page<RotaRequalificacaoResponse> findAll(Pageable pageable) {
        return rotaRepo.findAll(pageable).map(RotaRequalificacaoMapper::toDto);
    }

    @Transactional
    @Caching(
            put = { @CachePut(cacheNames = "rotasById", key = "#result.id") },
            evict = { @CacheEvict(cacheNames = "rotasList", allEntries = true) }
    )
    public RotaRequalificacaoResponse update(UUID id, UpdateRotaRequalificacaoRequest req) {
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
    public void delete(UUID id) {
        if (!rotaRepo.existsById(id)) throw new ResourceNotFoundException("Rota não encontrada");
        rotaRepo.deleteById(id);
    }
}
