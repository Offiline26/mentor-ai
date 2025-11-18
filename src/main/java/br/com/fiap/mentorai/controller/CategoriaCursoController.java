package br.com.fiap.mentorai.controller;

import br.com.fiap.mentorai.dto.*;
import br.com.fiap.mentorai.dto.request.create.CreateCategoriaCursoRequest;
import br.com.fiap.mentorai.dto.request.update.UpdateCategoriaCursoRequest;
import br.com.fiap.mentorai.mapper.CategoriaCursoMapper;
import br.com.fiap.mentorai.model.CategoriaCurso;
import br.com.fiap.mentorai.repository.CategoriaCursoRepository;
import br.com.fiap.mentorai.service.CategoriaCursoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/categorias-curso")
@Validated
public class CategoriaCursoController {

    private final CategoriaCursoService service;
    private final CategoriaCursoRepository repo;

    public CategoriaCursoController(CategoriaCursoService service, CategoriaCursoRepository repo) {
        this.service = service;
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<CategoriaCursoDto> create(@Valid @RequestBody CreateCategoriaCursoRequest req) {
        CategoriaCursoDto resp = service.create(req);
        return ResponseEntity.created(URI.create("/api/categorias-curso/" + resp.getId())).body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaCursoDto> get(@PathVariable UUID id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<CategoriaCursoDto>> list(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CategoriaCurso> page = repo.findAll(pageable);
        return ResponseEntity.ok(page.map(CategoriaCursoMapper::toDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaCursoDto> update(@PathVariable UUID id, @Valid @RequestBody UpdateCategoriaCursoRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
