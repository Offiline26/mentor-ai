package br.com.fiap.mentorai.controller;


import br.com.fiap.mentorai.dto.ParceiroCursoDto;
import br.com.fiap.mentorai.dto.request.create.CreateParceiroCursoRequest;
import br.com.fiap.mentorai.dto.request.update.UpdateParceiroCursoRequest;
import br.com.fiap.mentorai.service.ParceiroCursoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/parceiros")
@Validated
public class ParceiroCursoController {

    private final ParceiroCursoService service;

    public ParceiroCursoController(ParceiroCursoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ParceiroCursoDto> create(@Valid @RequestBody CreateParceiroCursoRequest req) {
        ParceiroCursoDto resp = service.create(req);
        return ResponseEntity.created(URI.create("/api/parceiros/" + resp.getId())).body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParceiroCursoDto> get(@PathVariable UUID id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<ParceiroCursoDto>> list(@PageableDefault(size = 20, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParceiroCursoDto> update(@PathVariable UUID id, @Valid @RequestBody UpdateParceiroCursoRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
