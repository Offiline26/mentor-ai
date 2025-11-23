package br.com.fiap.mentorai.repository;

import br.com.fiap.mentorai.model.RotaRequalificacao;
import br.com.fiap.mentorai.model.Usuario;
import br.com.fiap.mentorai.model.UsuarioRota;
import br.com.fiap.mentorai.model.UsuarioRotaId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRotaRepository extends JpaRepository<UsuarioRota, UsuarioRotaId> {

    boolean existsByUsuarioAndRota(Usuario usuario, RotaRequalificacao rota);
}
