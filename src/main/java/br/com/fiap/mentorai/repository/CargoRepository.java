package br.com.fiap.mentorai.repository;

import br.com.fiap.mentorai.model.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CargoRepository extends JpaRepository<Cargo, Long> {}