package br.com.fiap.mentorai.controller;

import br.com.fiap.mentorai.dto.*;
import br.com.fiap.mentorai.dto.request.CreateCursoRequest;
import br.com.fiap.mentorai.dto.request.UpdateCursoRequest;
import br.com.fiap.mentorai.dto.response.CursoResponse;
import br.com.fiap.mentorai.mapper.CursoMapper;
import br.com.fiap.mentorai.model.Curso;
import br.com.fiap.mentorai.repository.CursoRepository;
import br.com.fiap.mentorai.service.CursoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/cursos")
@Validated
public class CursoController {

    private final CursoService service;
    private final CursoRepository repo;

    public CursoController(CursoService service, CursoRepository repo) {
        this.service = service;
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<CursoResponse> create(@Valid @RequestBody CreateCursoRequest req) {
        CursoResponse resp = service.create(req);
        return ResponseEntity.created(URI.create("/api/cursos/" + resp.getId())).body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<CursoResponse>> list(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Curso> page = repo.findAll(pageable);
        return ResponseEntity.ok(page.map(CursoMapper::toDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoResponse> update(@PathVariable Long id,
                                                @Valid @RequestBody UpdateCursoRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
