package br.com.fiap.mentorai.service;



import br.com.fiap.mentorai.dto.request.create.CreateUsuarioRequest;

import br.com.fiap.mentorai.dto.request.update.UpdateUsuarioRequest;

import br.com.fiap.mentorai.dto.response.UsuarioResponse;

import br.com.fiap.mentorai.dto.upsert.UsuarioHabilidadeUpsert;

import br.com.fiap.mentorai.exception.ResourceConflictException;
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

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import java.util.UUID;



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

            e.setSenha(passwordEncoder.encode(req.getSenha()));

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
public UsuarioResponse get(UUID id) {
    // 🛑 USO DO REPOSITORY OTIMIZADO: findByIdWithDetails
    Usuario e = usuarioRepo.findByIdWithDetails(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    return UsuarioMapper.toDto(e);
}



    @Cacheable(cacheNames = "usuariosList")

    public Page<UsuarioResponse> findAll(Pageable pageable) {

        return usuarioRepo.findAll(pageable).map(UsuarioMapper::toDto);

    }



    @Transactional

    @Caching(

            put = { @CachePut(cacheNames = "usuariosById", key = "#result.id") },

            evict = { @CacheEvict(cacheNames = "usuariosList", allEntries = true) }

    )

    public UsuarioResponse update(UUID id, UpdateUsuarioRequest req) {

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

    public void delete(UUID id) {

        if (!usuarioRepo.existsById(id)) {

            throw new ResourceNotFoundException("Usuário não encontrado");

        }

        usuarioRepo.deleteById(id);

    }



    // -------- Rotas do usuário (não cacheei, pois é ação de domínio que mexe em progresso) --------
    @Transactional
    @CacheEvict(cacheNames = "usuariosById", key = "#idUsuario")
    public UsuarioResponse iniciarRota(UUID idUsuario, UUID idRota) {
        // 1. Valida se o usuário existe
        Usuario user = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        // 2. Valida se a rota existe
        RotaRequalificacao rota = rotaRepo.findById(idRota)
                .orElseThrow(() -> new ResourceNotFoundException("Rota não encontrada"));

        // 3. CRÍTICO: Verifica existência usando o ID Composto correto
        // Com o @EqualsAndHashCode na classe ID, isso agora vai encontrar o objeto se ele já estiver na sessão
        UsuarioRotaId idVinculo = new UsuarioRotaId(idUsuario, idRota);

        if (usuarioRotaRepo.existsById(idVinculo)) {
            // Se já existe, lança o 409 Conflict e aborta a transação
            throw new ResourceConflictException("O usuário já está realizando esta rota de requalificação.");
        }

        // 4. Se não existe, cria e salva
        UsuarioRota ur = UsuarioRota.builder()
                .usuario(user)
                .rota(rota)
                .dataInicio(LocalDateTime.now())
                .progressoPercentual(BigDecimal.ZERO)
                .build();

        usuarioRotaRepo.save(ur);

        // 5. Retorno
        // Não precisamos fazer user.getRotas().add(ur) aqui, pois o @CachePut
        // vai serializar o 'user' que buscamos no início.
        // Na próxima chamada GET, o Hibernate vai trazer a lista atualizada do banco.
        return UsuarioMapper.toDto(user);
    }

    @Transactional
    @CacheEvict(cacheNames = "usuariosById", key = "#idUsuario")
    public UsuarioResponse atualizarProgresso(UUID idUsuario, UUID idRota, BigDecimal novoProgresso) {
        // 1. Busca o relacionamento específico usando o ID Composto
        UsuarioRotaId idVinculo = new UsuarioRotaId(idUsuario, idRota);

        UsuarioRota ur = usuarioRotaRepo.findById(idVinculo)
                .orElseThrow(() -> new ResourceNotFoundException("O usuário não iniciou esta rota."));

        // 2. Atualiza o progresso
        ur.setProgressoPercentual(novoProgresso);

        // 3. Lógica de Conclusão Automática
        if (novoProgresso.compareTo(new BigDecimal("100.00")) >= 0) {
            if (ur.getDataConclusao() == null) {
                ur.setDataConclusao(LocalDateTime.now()); // Marca como concluído hoje
            }
        } else {
            ur.setDataConclusao(null); // Se desmarcou algo, reabre a rota
        }

        usuarioRotaRepo.save(ur);

        // 4. Retorna o usuário atualizado para o Cache
        return UsuarioMapper.toDto(ur.getUsuario());
    }

}
