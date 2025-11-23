package br.com.fiap.mentorai.controller;

import br.com.fiap.mentorai.dto.request.create.CreateUsuarioRequest;
import br.com.fiap.mentorai.dto.request.update.UpdateUsuarioRequest;
import br.com.fiap.mentorai.dto.response.UsuarioResponse;
import br.com.fiap.mentorai.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
@Validated
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> create(@Valid @RequestBody CreateUsuarioRequest req) {
        UsuarioResponse resp = service.create(req);
        URI location = URI.create("/api/usuarios/" + resp.getId());
        return ResponseEntity.created(location).body(resp);
    }

    @GetMapping("/{id}")
    // 🛑 AJUSTE CRÍTICO: Usa o bean para checar se o ID na URL (#id) é do usuário logado
    @PreAuthorize("@usuarioSecurity.isOwner(#id)")
    public ResponseEntity<UsuarioResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<UsuarioResponse>> list(@PageableDefault(size = 20, sort = "nomeRota") Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@usuarioSecurity.isOwner(#id)") // Aplique também em PUT e DELETE
    public ResponseEntity<UsuarioResponse> update(@PathVariable UUID id, @RequestBody @Valid UpdateUsuarioRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@usuarioSecurity.isOwner(#id)") // Aplique também em PUT e DELETE
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // O método iniciarRota precisa do ID do usuário, então a regra também deve ser aplicada
    @PostMapping("/{idUsuario}/rotas/{idRota}/iniciar")
    @PreAuthorize("@usuarioSecurity.isOwner(#idUsuario)")
    public ResponseEntity<UsuarioResponse> iniciarRota(@PathVariable UUID idUsuario, @PathVariable UUID idRota) {
        return ResponseEntity.ok(service.iniciarRota(idUsuario, idRota));
    }
}
