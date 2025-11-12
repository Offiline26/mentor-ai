package br.com.fiap.mentorai.repository;

import br.com.fiap.mentorai.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {}
