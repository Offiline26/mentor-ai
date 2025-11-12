package br.com.fiap.mentorai.repository;

import br.com.fiap.mentorai.model.UsuarioHabilidade;
import br.com.fiap.mentorai.model.UsuarioHabilidadeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioHabilidadeRepository extends JpaRepository<UsuarioHabilidade, UsuarioHabilidadeId> {}