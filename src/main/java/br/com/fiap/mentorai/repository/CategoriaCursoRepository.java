package br.com.fiap.mentorai.repository;

import br.com.fiap.mentorai.model.CategoriaCurso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoriaCursoRepository extends JpaRepository<CategoriaCurso, UUID> {}
