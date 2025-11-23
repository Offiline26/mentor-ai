package br.com.fiap.mentorai.controller;

import br.com.fiap.mentorai.dto.*;
import br.com.fiap.mentorai.dto.request.create.CreateCategoriaHabilidadeRequest;
import br.com.fiap.mentorai.dto.request.update.UpdateCategoriaHabilidadeRequest;
import br.com.fiap.mentorai.service.CategoriaHabilidadeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/categorias-habilidade")
@Validated
public class CategoriaHabilidadeController {

    private final CategoriaHabilidadeService service;

    public CategoriaHabilidadeController(CategoriaHabilidadeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CategoriaHabilidadeDto> create(@Valid @RequestBody CreateCategoriaHabilidadeRequest req) {
        CategoriaHabilidadeDto resp = service.create(req);
        return ResponseEntity.created(URI.create("/api/categorias-habilidade/" + resp.getId())).body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaHabilidadeDto> get(@PathVariable UUID id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<CategoriaHabilidadeDto>> list(@PageableDefault(size = 20, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaHabilidadeDto> update(@PathVariable UUID id, @Valid @RequestBody UpdateCategoriaHabilidadeRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
