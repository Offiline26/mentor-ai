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

    public UsuarioService(UsuarioRepository usuarioRepo,
                          CargoRepository cargoRepo,
                          AreaAtuacaoRepository areaRepo,
                          HabilidadeRepository habilidadeRepo,
                          UsuarioHabilidadeRepository usuarioHabRepo,
                          RotaRequalificacaoRepository rotaRepo,
                          UsuarioRotaRepository usuarioRotaRepo) {
        this.usuarioRepo = usuarioRepo;
        this.cargoRepo = cargoRepo;
        this.areaRepo = areaRepo;
        this.habilidadeRepo = habilidadeRepo;
        this.usuarioHabRepo = usuarioHabRepo;
        this.rotaRepo = rotaRepo;
        this.usuarioRotaRepo = usuarioRotaRepo;
    }

    // -------- CRUD --------

    @Transactional
    public UsuarioResponse create(CreateUsuarioRequest req) {
        Usuario e = UsuarioMapper.toEntity(req);

        // relacionamentos
        if (req.getIdCargo() != null) {
            e.setCargo(cargoRepo.findById(req.getIdCargo())
                    .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado")));
        }
        if (req.getIdAreaAtuacao() != null) {
            e.setAreaAtuacao(areaRepo.findById(req.getIdAreaAtuacao())
                    .orElseThrow(() -> new ResourceNotFoundException("Área de atuação não encontrada")));
        }

        // senha: gerar hash no futuro (ex.: BCrypt), por ora manter como veio? Ajuste conforme sua regra
        if (req.getSenha() != null) {
            e.setSenhaHash(req.getSenha()); // TODO: encoder
        }

        e = usuarioRepo.save(e);

        // habilidades iniciais (opcional)
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

    public UsuarioResponse get(Long id) {
        Usuario e = usuarioRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        return UsuarioMapper.toDto(e);
    }

    public List<UsuarioResponse> list() {
        return UsuarioMapper.toDtoList(usuarioRepo.findAll());
    }

    @Transactional
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

        // Upsert de habilidades (se vier no request)
        if (req.getHabilidades() != null) {
            // estratégia simples: limpa e recria
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
    public void delete(Long id) {
        if (!usuarioRepo.existsById(id)) {
            throw new ResourceNotFoundException("Usuário não encontrado");
        }
        usuarioRepo.deleteById(id);
    }

    // -------- Rotas do usuário --------

    @Transactional
    public UsuarioResponse iniciarRota(Long idUsuario, Long idRota) {
        Usuario user = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        RotaRequalificacao rota = rotaRepo.findById(idRota)
                .orElseThrow(() -> new ResourceNotFoundException("Rota não encontrada"));

        // evita duplicidade
        boolean jaTem = user.getRotas().stream().anyMatch(ur -> ur.getRota().getId().equals(idRota));
        if (!jaTem) {
            UsuarioRota ur = user.iniciarRota(rota);
            usuarioRotaRepo.save(ur);
        }
        return UsuarioMapper.toDto(user);
    }
}