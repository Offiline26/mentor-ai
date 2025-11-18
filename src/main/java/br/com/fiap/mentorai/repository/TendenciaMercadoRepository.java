package br.com.fiap.mentorai.repository;

import br.com.fiap.mentorai.model.TendenciaMercado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TendenciaMercadoRepository extends JpaRepository<TendenciaMercado, UUID> {}
