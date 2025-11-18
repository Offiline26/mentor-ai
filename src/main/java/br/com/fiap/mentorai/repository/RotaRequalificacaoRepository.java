package br.com.fiap.mentorai.repository;

import br.com.fiap.mentorai.model.RotaRequalificacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RotaRequalificacaoRepository extends JpaRepository<RotaRequalificacao, UUID> {}