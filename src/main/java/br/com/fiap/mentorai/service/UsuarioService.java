package br.com.fiap.mentorai.service;

import br.com.fiap.mentorai.dto.*;
import br.com.fiap.mentorai.dto.request.CreateUsuarioRequest;
import br.com.fiap.mentorai.dto.request.UpdateUsuarioRequest;
import br.com.fiap.mentorai.dto.response.UsuarioResponse;
import br.com.fiap.mentorai.dto.upsert.UsuarioHabilidadeUpsert;
import br.com.fiap.mentorai.exception.ResourceNotFoundException;
import br.com.fiap.mentorai.mapper.UsuarioMapper;
import br.com.fiap.mentorai.model.*;
import br.com.fiap.mentorai.model.enums.NivelProficienciaEnum;
import br.com.fiap.mentorai.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepo;
    private final CargoRepository cargoRepo;
    private final AreaAtuacaoRepository areaRepo;
    private final HabilidadeRepository habilidadeRepo;
    private final UsuarioHabilidadeRepository usuarioHabRepo;
    private final RotaRequalificacaoRepository rotaRepo;
    private final UsuarioRotaRepository usuarioRotaRepo;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepo,
                          CargoRepository cargoRepo,
                          AreaAtuacaoRepository areaRepo,
                          HabilidadeRepository habilidadeRepo,
                          UsuarioHabilidadeRepository usuarioHabRepo,
                          RotaRequalificacaoRepository rotaRepo,
                          UsuarioRotaRepository usuarioRotaRepo,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepo = usuarioRepo;
        this.cargoRepo = cargoRepo;
        this.areaRepo = areaRepo;
        this.habilidadeRepo = habilidadeRepo;
        this.usuarioHabRepo = usuarioHabRepo;
        this.rotaRepo = rotaRepo;
        this.usuarioRotaRepo = usuarioRotaRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // -------- CRUD --------

    @Transactional
    @Caching(
            put = { @CachePut(cacheNames = "usuariosById", key = "#result.id") },
            evict = { @CacheEvict(cacheNames = "usuariosList", allEntries = true) }
    )
    public UsuarioResponse create(CreateUsuarioRequest req) {
        Usuario e = UsuarioMapper.toEntity(req);

        if (req.getIdCargo() != null) {
            e.setCargo(cargoRepo.findById(req.getIdCargo())
                    .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado")));
        }
        if (req.getIdAreaAtuacao() != null) {
            e.setAreaAtuacao(areaRepo.findById(req.getIdAreaAtuacao())
                    .orElseThrow(() -> new ResourceNotFoundException("Área de atuação não encontrada")));
        }

        if (req.getSenha() != null) {
            e.setSenhaHash(passwordEncoder.encode(req.getSenha()));
        }

        e = usuarioRepo.save(e);

        if (req.getHabilidades() != null) {
            for (UsuarioHabilidadeUpsert h : req.getHabilidades()) {
                Habilidade hab = habilidadeRepo.findById(h.getIdHabilidade())
                        .orElseThrow(() -> new ResourceNotFoundException("Habilidade não encontrada"));
                NivelProficienciaEnum nivel = UsuarioMapper.nivelFromInt(h.getNivel());
                UsuarioHabilidade uh = UsuarioHabilidade.builder()
                        .usuario(e)
                        .habilidade(hab)
                        .nivelProficiencia(nivel)
                        .build();
                usuarioHabRepo.save(uh);
                e.getHabilidades().add(uh);
            }
        }
        return UsuarioMapper.toDto(e);
    }

    @Cacheable(cacheNames = "usuariosById", key = "#id")
    public UsuarioResponse get(Long id) {
        Usuario e = usuarioRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        return UsuarioMapper.toDto(e);
    }

    @Cacheable(cacheNames = "usuariosList")
    public List<UsuarioResponse> list() {
        return UsuarioMapper.toDtoList(usuarioRepo.findAll());
    }

    @Transactional
    @Caching(
            put = { @CachePut(cacheNames = "usuariosById", key = "#result.id") },
            evict = { @CacheEvict(cacheNames = "usuariosList", allEntries = true) }
    )
    public UsuarioResponse update(Long id, UpdateUsuarioRequest req) {
        Usuario e = usuarioRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        UsuarioMapper.applyUpdate(req, e);

        if (req.getIdCargo() != null) {
            e.setCargo(cargoRepo.findById(req.getIdCargo())
                    .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado")));
        }
        if (req.getIdAreaAtuacao() != null) {
            e.setAreaAtuacao(areaRepo.findById(req.getIdAreaAtuacao())
                    .orElseThrow(() -> new ResourceNotFoundException("Área de atuação não encontrada")));
        }

        if (req.getHabilidades() != null) {
            usuarioHabRepo.deleteAll(e.getHabilidades());
            e.getHabilidades().clear();

            for (UsuarioHabilidadeUpsert h : req.getHabilidades()) {
                Habilidade hab = habilidadeRepo.findById(h.getIdHabilidade())
                        .orElseThrow(() -> new ResourceNotFoundException("Habilidade não encontrada"));
                NivelProficienciaEnum nivel = UsuarioMapper.nivelFromInt(h.getNivel());
                UsuarioHabilidade uh = UsuarioHabilidade.builder()
                        .usuario(e)
                        .habilidade(hab)
                        .nivelProficiencia(nivel)
                        .build();
                usuarioHabRepo.save(uh);
                e.getHabilidades().add(uh);
            }
        }

        return UsuarioMapper.toDto(e);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "usuariosById", key = "#id"),
            @CacheEvict(cacheNames = "usuariosList", allEntries = true)
    })
    public void delete(Long id) {
        if (!usuarioRepo.existsById(id)) {
            throw new ResourceNotFoundException("Usuário não encontrado");
        }
        usuarioRepo.deleteById(id);
    }

    // -------- Rotas do usuário (não cacheei, pois é ação de domínio que mexe em progresso) --------

    @Transactional
    public UsuarioResponse iniciarRota(Long idUsuario, Long idRota) {
        Usuario user = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        RotaRequalificacao rota = rotaRepo.findById(idRota)
                .orElseThrow(() -> new ResourceNotFoundException("Rota não encontrada"));

        boolean jaTem = user.getRotas().stream().anyMatch(ur -> ur.getRota().getId().equals(idRota));
        if (!jaTem) {
            UsuarioRota ur = user.iniciarRota(rota);
            usuarioRotaRepo.save(ur);
        }
        // opcional: poderia atualizar cache de usuariosById aqui com @CachePut se quisesse
        return UsuarioMapper.toDto(user);
    }
}