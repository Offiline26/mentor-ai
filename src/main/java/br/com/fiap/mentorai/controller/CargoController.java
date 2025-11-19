package br.com.fiap.mentorai.controller;

import br.com.fiap.mentorai.model.Cargo;
import br.com.fiap.mentorai.repository.CargoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cargos") // <<-- Essa rota tem que bater com o Security
public class CargoController {

    private final CargoRepository repository;

    public CargoController(CargoRepository repository) {
        this.repository = repository;
    }

    // Esse método será PÚBLICO graças ao SecurityConfig
    @GetMapping
    public ResponseEntity<List<Cargo>> listarTodos() {
        return ResponseEntity.ok(repository.findAll());
    }

    // Esse método continuará PRIVADO (precisa de Token)
    // porque liberamos apenas HttpMethod.GET
    @PostMapping
    public ResponseEntity<Cargo> criar(@RequestBody Cargo cargo) {
        return ResponseEntity.ok(repository.save(cargo));
    }
}