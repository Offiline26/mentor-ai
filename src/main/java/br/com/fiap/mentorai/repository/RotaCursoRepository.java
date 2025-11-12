package br.com.fiap.mentorai.repository;

import br.com.fiap.mentorai.model.RotaCurso;
import br.com.fiap.mentorai.model.RotaCursoId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RotaCursoRepository extends JpaRepository<RotaCurso, RotaCursoId> {}
