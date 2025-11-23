package br.com.fiap.mentorai.controller;

import br.com.fiap.mentorai.dto.request.create.CreateHabilidadeRequest;
import br.com.fiap.mentorai.dto.request.update.UpdateHabilidadeRequest;
import br.com.fiap.mentorai.dto.response.HabilidadeResponse;
import br.com.fiap.mentorai.service.HabilidadeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/habilidades")
@Validated
public class HabilidadeController {

    private final HabilidadeService service;

    public HabilidadeController(HabilidadeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<HabilidadeResponse> create(@Valid @RequestBody CreateHabilidadeRequest req) {
        var resp = service.create(req);
        return ResponseEntity.created(URI.create("/api/habilidades/" + resp.getId())).body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabilidadeResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<HabilidadeResponse>> list(@PageableDefault(size = 20, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HabilidadeResponse> update(@PathVariable UUID id,
                                                     @Valid @RequestBody UpdateHabilidadeRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
