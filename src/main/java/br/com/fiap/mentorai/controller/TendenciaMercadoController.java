package br.com.fiap.mentorai.controller;

import br.com.fiap.mentorai.dto.request.create.CreateTendenciaMercadoRequest;
import br.com.fiap.mentorai.dto.request.update.UpdateTendenciaMercadoRequest;
import br.com.fiap.mentorai.dto.response.TendenciaMercadoResponse;
import br.com.fiap.mentorai.service.TendenciaMercadoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/tendencias")
@Validated
public class TendenciaMercadoController {

    private final TendenciaMercadoService service;

    public TendenciaMercadoController(TendenciaMercadoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TendenciaMercadoResponse> create(@Valid @RequestBody CreateTendenciaMercadoRequest req) {
        var resp = service.create(req);
        return ResponseEntity.created(URI.create("/api/tendencias/" + resp.getId())).body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TendenciaMercadoResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<TendenciaMercadoResponse>> list(@PageableDefault(size = 20, sort = "dataAnalise", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TendenciaMercadoResponse> update(@PathVariable UUID id,
                                                           @Valid @RequestBody UpdateTendenciaMercadoRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}