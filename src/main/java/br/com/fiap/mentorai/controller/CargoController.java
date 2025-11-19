package br.com.fiap.mentorai.controller;

import br.com.fiap.mentorai.dto.CargoDto;
import br.com.fiap.mentorai.dto.request.create.CreateCargoRequest;
import br.com.fiap.mentorai.dto.request.update.UpdateCargoRequest;
import br.com.fiap.mentorai.service.CargoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cargos") // Bate com o Security: .requestMatchers(HttpMethod.GET, "/api/cargos/**").permitAll()
@Validated
public class CargoController {

    private final CargoService service;

    // Injeção de Dependência via Construtor (Padrão Sênior)
    public CargoController(CargoService service) {
        this.service = service;
    }

    // ==============================================================
    // 1. MÉTODO PÚBLICO (GET) - Liberado no SecurityConfig
    // ==============================================================
    @GetMapping
    public ResponseEntity<List<CargoDto>> list() {
        // Chama o service.list() que tem @Cacheable("cargosList")
        // Retorna JSON Array puro [{}, {}], perfeito para o React Native
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CargoDto> get(@PathVariable UUID id) {
        return ResponseEntity.ok(service.get(id));
    }

    // ==============================================================
    // 2. MÉTODOS PRIVADOS (POST/PUT/DELETE) - Exigem Token JWT
    // ==============================================================
    @PostMapping
    public ResponseEntity<CargoDto> create(@Valid @RequestBody CreateCargoRequest req) {
        CargoDto created = service.create(req);
        return ResponseEntity.created(URI.create("/api/cargos/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CargoDto> update(@PathVariable UUID id,
                                           @Valid @RequestBody UpdateCargoRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
