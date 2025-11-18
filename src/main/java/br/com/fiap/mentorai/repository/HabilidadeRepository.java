package br.com.fiap.mentorai.repository;

import br.com.fiap.mentorai.model.Habilidade;
import br.com.fiap.mentorai.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HabilidadeRepository extends JpaRepository<Habilidade, UUID> {
    Optional<Habilidade> findById(UUID id);
}
