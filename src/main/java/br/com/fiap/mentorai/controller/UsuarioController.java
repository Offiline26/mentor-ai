package br.com.fiap.mentorai.controller;

import br.com.fiap.mentorai.dto.*;
import br.com.fiap.mentorai.dto.request.CreateUsuarioRequest;
import br.com.fiap.mentorai.dto.request.UpdateUsuarioRequest;
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
    public ResponseEntity<UsuarioResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<UsuarioResponse>> list(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Usuario> page = repo.findAll(pageable);
        Page<UsuarioResponse> mapped = page.map(u -> service.get(u.getId())); // reaproveita service/mappers
        return ResponseEntity.ok(mapped);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody UpdateUsuarioRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Ações de domínio
    @PostMapping("/{idUsuario}/rotas/{idRota}:iniciar")
    public ResponseEntity<UsuarioResponse> iniciarRota(@PathVariable Long idUsuario, @PathVariable Long idRota) {
        return ResponseEntity.ok(service.iniciarRota(idUsuario, idRota));
    }
}
