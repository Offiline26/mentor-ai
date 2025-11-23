package br.com.fiap.mentorai.controller;

import br.com.fiap.mentorai.dto.*;
import br.com.fiap.mentorai.dto.request.create.CreateAreaAtuacaoRequest;
import br.com.fiap.mentorai.dto.request.update.UpdateAreaAtuacaoRequest;
import br.com.fiap.mentorai.service.AreaAtuacaoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/areas")
@Validated
public class AreaAtuacaoController {

    private final AreaAtuacaoService service;

    public AreaAtuacaoController(AreaAtuacaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AreaAtuacaoDto> create(@Valid @RequestBody CreateAreaAtuacaoRequest req) {
        AreaAtuacaoDto resp = service.create(req);
        return ResponseEntity.created(URI.create("/api/areas/" + resp.getId())).body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AreaAtuacaoDto> get(@PathVariable UUID id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<AreaAtuacaoDto>> list(@PageableDefault(size = 20, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AreaAtuacaoDto> update(@PathVariable UUID id, @Valid @RequestBody UpdateAreaAtuacaoRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}