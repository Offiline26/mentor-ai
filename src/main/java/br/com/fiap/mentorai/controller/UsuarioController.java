package br.com.fiap.mentorai.controller;

import br.com.fiap.mentorai.dto.request.create.CreateUsuarioRequest;
import br.com.fiap.mentorai.dto.request.update.UpdateUsuarioRequest;
import br.com.fiap.mentorai.dto.response.HabilidadeResponse;
import br.com.fiap.mentorai.dto.response.UsuarioResponse;
import br.com.fiap.mentorai.model.Usuario;
import br.com.fiap.mentorai.repository.UsuarioRepository;
import br.com.fiap.mentorai.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
@Validated
public class UsuarioController {

    private final UsuarioService service;
    private final UsuarioRepository repo;

    public UsuarioController(UsuarioService service, UsuarioRepository repo) {
        this.service = service;
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> create(@Valid @RequestBody CreateUsuarioRequest req) {
        UsuarioResponse resp = service.create(req);
        URI location = URI.create("/api/usuarios/" + resp.getId());
        return ResponseEntity.created(location).body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> list() {
        // CORREÇÃO: Usa service.list() (Cacheado e retorna Lista simples para as Tags do Mobile)
        return ResponseEntity.ok(service.list());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> update(@PathVariable UUID id,
                                                  @Valid @RequestBody UpdateUsuarioRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Ações de domínio
    @PostMapping("/{idUsuario}/rotas/{idRota}:iniciar")
    public ResponseEntity<UsuarioResponse> iniciarRota(@PathVariable UUID idUsuario, @PathVariable UUID idRota) {
        return ResponseEntity.ok(service.iniciarRota(idUsuario, idRota));
    }
}
