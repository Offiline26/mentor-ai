package br.com.fiap.mentorai.controller;

import br.com.fiap.mentorai.dto.request.create.CreateHabilidadeRequest;
import br.com.fiap.mentorai.dto.request.update.UpdateHabilidadeRequest;
import br.com.fiap.mentorai.dto.response.HabilidadeResponse;
import br.com.fiap.mentorai.mapper.HabilidadeMapper;
import br.com.fiap.mentorai.model.Habilidade;
import br.com.fiap.mentorai.repository.HabilidadeRepository;
import br.com.fiap.mentorai.service.HabilidadeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/habilidades")
@Validated
public class HabilidadeController {

    private final HabilidadeService service;
    private final HabilidadeRepository repo;

    public HabilidadeController(HabilidadeService service, HabilidadeRepository repo) {
        this.service = service;
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<HabilidadeResponse> create(@Valid @RequestBody CreateHabilidadeRequest req) {
        var resp = service.create(req);
        return ResponseEntity.created(URI.create("/api/habilidades/" + resp.getId())).body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabilidadeResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<HabilidadeResponse>> list(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Habilidade> page = repo.findAll(pageable);
        return ResponseEntity.ok(page.map(HabilidadeMapper::toDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HabilidadeResponse> update(@PathVariable Long id,
                                                     @Valid @RequestBody UpdateHabilidadeRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
