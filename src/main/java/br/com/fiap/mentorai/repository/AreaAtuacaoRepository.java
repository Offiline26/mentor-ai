package br.com.fiap.mentorai.repository;

import br.com.fiap.mentorai.model.AreaAtuacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AreaAtuacaoRepository extends JpaRepository<AreaAtuacao, UUID> {}