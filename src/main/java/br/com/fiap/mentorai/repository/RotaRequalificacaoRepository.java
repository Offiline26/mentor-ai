package br.com.fiap.mentorai.repository;

import br.com.fiap.mentorai.model.RotaRequalificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface RotaRequalificacaoRepository extends JpaRepository<RotaRequalificacao, UUID> {

    // 🚀 OTIMIZAÇÃO: Busca a Rota E a lista de Cursos (Trilha) de uma vez só.
    // Resolve o LazyInitializationException e evita o Erro 500.
    @Query("SELECT r FROM RotaRequalificacao r LEFT JOIN FETCH r.cursos rc LEFT JOIN FETCH rc.curso c WHERE r.id = :id")
    Optional<RotaRequalificacao> findByIdWithCursos(@Param("id") UUID id);
}
