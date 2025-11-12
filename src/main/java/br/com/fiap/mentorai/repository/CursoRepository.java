package br.com.fiap.mentorai.repository;

import br.com.fiap.mentorai.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {}