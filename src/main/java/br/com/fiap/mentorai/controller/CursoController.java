package br.com.fiap.mentorai.controller;

import br.com.fiap.mentorai.dto.request.create.CreateCursoRequest;
import br.com.fiap.mentorai.dto.request.update.UpdateCursoRequest;
import br.com.fiap.mentorai.dto.response.CursoResponse;
import br.com.fiap.mentorai.service.CursoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/cursos")
@Validated
public class CursoController {

    private final CursoService service;

    public CursoController(CursoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CursoResponse> create(@Valid @RequestBody CreateCursoRequest req) {
        CursoResponse resp = service.create(req);
        return ResponseEntity.created(URI.create("/api/cursos/" + resp.getId())).body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<CursoResponse>> list(@PageableDefault(size = 20, sort = "titulo") Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoResponse> update(@PathVariable UUID id,
                                                @Valid @RequestBody UpdateCursoRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
