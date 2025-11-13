package br.com.fiap.mentorai.controller;

import br.com.fiap.mentorai.dto.*;
import br.com.fiap.mentorai.dto.request.create.CreateAreaAtuacaoRequest;
import br.com.fiap.mentorai.dto.request.update.UpdateAreaAtuacaoRequest;
import br.com.fiap.mentorai.mapper.AreaAtuacaoMapper;
import br.com.fiap.mentorai.model.AreaAtuacao;
import br.com.fiap.mentorai.repository.AreaAtuacaoRepository;
import br.com.fiap.mentorai.service.AreaAtuacaoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/areas")
@Validated
public class AreaAtuacaoController {

    private final AreaAtuacaoService service;
    private final AreaAtuacaoRepository repo;

    public AreaAtuacaoController(AreaAtuacaoService service, AreaAtuacaoRepository repo) {
        this.service = service;
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<AreaAtuacaoDto> create(@Valid @RequestBody CreateAreaAtuacaoRequest req) {
        AreaAtuacaoDto resp = service.create(req);
        return ResponseEntity.created(URI.create("/api/areas/" + resp.getId())).body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AreaAtuacaoDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<AreaAtuacaoDto>> list(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<AreaAtuacao> page = repo.findAll(pageable);
        return ResponseEntity.ok(page.map(AreaAtuacaoMapper::toDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AreaAtuacaoDto> update(@PathVariable Long id, @Valid @RequestBody UpdateAreaAtuacaoRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}