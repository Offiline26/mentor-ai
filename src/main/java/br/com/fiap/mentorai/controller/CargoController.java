package br.com.fiap.mentorai.controller;

import br.com.fiap.mentorai.dto.CargoDto;
import br.com.fiap.mentorai.dto.request.create.CreateCargoRequest;
import br.com.fiap.mentorai.dto.request.update.UpdateCargoRequest;
import br.com.fiap.mentorai.service.CargoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/cargos")
@Validated
public class CargoController {

    private final CargoService service;

    public CargoController(CargoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<CargoDto>> list(@PageableDefault(size = 20, sort = "nomeCargo") Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CargoDto> get(@PathVariable UUID id) {
        return ResponseEntity.ok(service.get(id));
    }

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
