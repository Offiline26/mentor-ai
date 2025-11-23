package br.com.fiap.mentorai.controller;

import br.com.fiap.mentorai.dto.request.create.CreateRotaRequalificacaoRequest;
import br.com.fiap.mentorai.dto.request.update.UpdateRotaRequalificacaoRequest;
import br.com.fiap.mentorai.dto.response.RotaRequalificacaoResponse;
import br.com.fiap.mentorai.service.RotaRequalificacaoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/rotas")
@Validated
public class RotaRequalificacaoController {

    private final RotaRequalificacaoService service;

    public RotaRequalificacaoController(RotaRequalificacaoService service) {
        this.service = service;

    }

    @PostMapping
    public ResponseEntity<RotaRequalificacaoResponse> create(@Valid @RequestBody CreateRotaRequalificacaoRequest req) {
        var resp = service.create(req);
        return ResponseEntity.created(URI.create("/api/rotas/" + resp.getId())).body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RotaRequalificacaoResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<RotaRequalificacaoResponse>> list(@PageableDefault(size = 20, sort = "nomeRota") Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RotaRequalificacaoResponse> update(@PathVariable UUID id,
                                                             @Valid @RequestBody UpdateRotaRequalificacaoRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
