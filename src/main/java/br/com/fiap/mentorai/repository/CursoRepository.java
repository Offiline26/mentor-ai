package br.com.fiap.mentorai.repository;

import br.com.fiap.mentorai.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CursoRepository extends JpaRepository<Curso, UUID> {}