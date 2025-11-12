package br.com.fiap.mentorai.controller;

import br.com.fiap.mentorai.dto.*;
import br.com.fiap.mentorai.dto.request.CreateCategoriaHabilidadeRequest;
import br.com.fiap.mentorai.dto.request.UpdateCategoriaHabilidadeRequest;
import br.com.fiap.mentorai.mapper.CategoriaHabilidadeMapper;
import br.com.fiap.mentorai.model.CategoriaHabilidade;
import br.com.fiap.mentorai.repository.CategoriaHabilidadeRepository;
import br.com.fiap.mentorai.service.CategoriaHabilidadeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/categorias-habilidade")
@Validated
public class CategoriaHabilidadeController {

    private final CategoriaHabilidadeService service;
    private final CategoriaHabilidadeRepository repo;

    public CategoriaHabilidadeController(CategoriaHabilidadeService service, CategoriaHabilidadeRepository repo) {
        this.service = service;
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<CategoriaHabilidadeDto> create(@Valid @RequestBody CreateCategoriaHabilidadeRequest req) {
        CategoriaHabilidadeDto resp = service.create(req);
        return ResponseEntity.created(URI.create("/api/categorias-habilidade/" + resp.getId())).body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaHabilidadeDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<CategoriaHabilidadeDto>> list(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CategoriaHabilidade> page = repo.findAll(pageable);
        return ResponseEntity.ok(page.map(CategoriaHabilidadeMapper::toDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaHabilidadeDto> update(@PathVariable Long id, @Valid @RequestBody UpdateCategoriaHabilidadeRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
