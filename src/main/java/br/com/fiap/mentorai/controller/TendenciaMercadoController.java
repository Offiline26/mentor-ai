package br.com.fiap.mentorai.controller;

import br.com.fiap.mentorai.dto.request.create.CreateTendenciaMercadoRequest;
import br.com.fiap.mentorai.dto.request.update.UpdateTendenciaMercadoRequest;
import br.com.fiap.mentorai.dto.response.TendenciaMercadoResponse;
import br.com.fiap.mentorai.mapper.TendenciaMercadoMapper;
import br.com.fiap.mentorai.model.TendenciaMercado;
import br.com.fiap.mentorai.repository.TendenciaMercadoRepository;
import br.com.fiap.mentorai.service.TendenciaMercadoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/tendencias")
@Validated
public class TendenciaMercadoController {

    private final TendenciaMercadoService service;
    private final TendenciaMercadoRepository repo;

    public TendenciaMercadoController(TendenciaMercadoService service, TendenciaMercadoRepository repo) {
        this.service = service;
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<TendenciaMercadoResponse> create(@Valid @RequestBody CreateTendenciaMercadoRequest req) {
        var resp = service.create(req);
        return ResponseEntity.created(URI.create("/api/tendencias/" + resp.getId())).body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TendenciaMercadoResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<TendenciaMercadoResponse>> list(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TendenciaMercado> page = repo.findAll(pageable);
        return ResponseEntity.ok(page.map(TendenciaMercadoMapper::toDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TendenciaMercadoResponse> update(@PathVariable Long id,
                                                           @Valid @RequestBody UpdateTendenciaMercadoRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}