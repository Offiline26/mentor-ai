package br.com.fiap.mentorai.controller;

import br.com.fiap.mentorai.model.*;
import br.com.fiap.mentorai.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dados")
public class DadosAuxiliaresController {

    private final CargoRepository cargoRepo;
    private final AreaAtuacaoRepository areaRepo;
    private final HabilidadeRepository habilidadeRepo;

    public DadosAuxiliaresController(CargoRepository cargoRepo,
                                     AreaAtuacaoRepository areaRepo,
                                     HabilidadeRepository habilidadeRepo) {
        this.cargoRepo = cargoRepo;
        this.areaRepo = areaRepo;
        this.habilidadeRepo = habilidadeRepo;
    }

    @GetMapping("/cargos")
    public ResponseEntity<List<Cargo>> listarCargos() {
        return ResponseEntity.ok(cargoRepo.findAll());
    }

    @GetMapping("/areas")
    public ResponseEntity<List<AreaAtuacao>> listarAreas() {
        return ResponseEntity.ok(areaRepo.findAll());
    }

    @GetMapping("/habilidades")
    public ResponseEntity<List<Habilidade>> listarHabilidades() {
        return ResponseEntity.ok(habilidadeRepo.findAll());
    }
}
