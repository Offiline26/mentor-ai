package br.com.fiap.mentorai.repository;

import br.com.fiap.mentorai.model.ParceiroCurso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ParceiroCursoRepository extends JpaRepository<ParceiroCurso, UUID> {}
