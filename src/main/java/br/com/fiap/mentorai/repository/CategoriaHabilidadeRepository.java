package br.com.fiap.mentorai.repository;

import br.com.fiap.mentorai.model.CategoriaHabilidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoriaHabilidadeRepository extends JpaRepository<CategoriaHabilidade, UUID> {}
