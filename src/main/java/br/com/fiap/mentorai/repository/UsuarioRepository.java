package br.com.fiap.mentorai.repository;

import br.com.fiap.mentorai.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findByEmail(String email);

    List<Usuario> email(String email);


    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.habilidades uh LEFT JOIN FETCH u.rotas ur WHERE u.id = :id")
    Optional<Usuario> findByIdWithDetails(@Param("id") UUID id);
    
    /**
     * Chama a função Oracle FN_VALIDAR_EMAIL_FORMATO.
     * @param email O email a ser validado.
     * @return 1 se válido, 0 se inválido.
     */
    @Procedure(name = "FN_VALIDAR_EMAIL_FORMATO") // Nome da Função/Procedure no Banco
    Integer validarEmailFormato(@Param("p_email") String email);
}
