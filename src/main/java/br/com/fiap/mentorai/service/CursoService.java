package br.com.fiap.mentorai.service;

import br.com.fiap.mentorai.dto.*;
import br.com.fiap.mentorai.dto.request.CreateCursoRequest;
import br.com.fiap.mentorai.dto.request.UpdateCursoRequest;
import br.com.fiap.mentorai.dto.response.CursoResponse;
import br.com.fiap.mentorai.exception.ResourceNotFoundException;
import br.com.fiap.mentorai.mapper.CursoMapper;
import br.com.fiap.mentorai.model.Curso;
import br.com.fiap.mentorai.model.Habilidade;
import br.com.fiap.mentorai.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class CursoService {

    private final CursoRepository cursoRepo;
    private final ParceiroCursoRepository parceiroRepo;
    private final CategoriaCursoRepository categoriaRepo;
    private final HabilidadeRepository habilidadeRepo;

    public CursoService(CursoRepository cursoRepo,
                        ParceiroCursoRepository parceiroRepo,
                        CategoriaCursoRepository categoriaRepo,
                        HabilidadeRepository habilidadeRepo) {
        this.cursoRepo = cursoRepo;
        this.parceiroRepo = parceiroRepo;
        this.categoriaRepo = categoriaRepo;
        this.habilidadeRepo = habilidadeRepo;
    }

    @Transactional
    public CursoResponse create(CreateCursoRequest req) {
        Curso e = CursoMapper.toEntity(req);

        if (req.getIdParceiro() != null) {
            e.setParceiro(parceiroRepo.findById(req.getIdParceiro())
                    .orElseThrow(() -> new ResourceNotFoundException("Parceiro não encontrado")));
        }
        if (req.getIdCategoriaCurso() != null) {
            e.setCategoriaCurso(categoriaRepo.findById(req.getIdCategoriaCurso())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria de curso não encontrada")));
        }
        if (req.getHabilidades() != null) {
            e.setHabilidades(new HashSet<>());
            for (Long idHab : req.getHabilidades()) {
                Habilidade h = habilidadeRepo.findById(idHab)
                        .orElseThrow(() -> new ResourceNotFoundException("Habilidade não encontrada: " + idHab));
                e.getHabilidades().add(h);
            }
        }

        e = cursoRepo.save(e);
        return CursoMapper.toDto(e);
    }

    public CursoResponse get(Long id) {
        Curso e = cursoRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado"));
        return CursoMapper.toDto(e);
    }

    public List<CursoResponse> list() {
        return CursoMapper.toDtoList(cursoRepo.findAll());
    }

    @Transactional
    public CursoResponse update(Long id, UpdateCursoRequest req) {
        Curso e = cursoRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado"));

        CursoMapper.applyUpdate(req, e);

        if (req.getIdParceiro() != null) {
            e.setParceiro(parceiroRepo.findById(req.getIdParceiro())
                    .orElseThrow(() -> new ResourceNotFoundException("Parceiro não encontrado")));
        }
        if (req.getIdCategoriaCurso() != null) {
            e.setCategoriaCurso(categoriaRepo.findById(req.getIdCategoriaCurso())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria de curso não encontrada")));
        }
        if (req.getHabilidades() != null) {
            e.getHabilidades().clear();
            for (Long idHab : req.getHabilidades()) {
                Habilidade h = habilidadeRepo.findById(idHab)
                        .orElseThrow(() -> new ResourceNotFoundException("Habilidade não encontrada: " + idHab));
                e.getHabilidades().add(h);
            }
        }
        return CursoMapper.toDto(e);
    }

    @Transactional
    public void delete(Long id) {
        if (!cursoRepo.existsById(id)) throw new ResourceNotFoundException("Curso não encontrado");
        cursoRepo.deleteById(id);
    }
}
