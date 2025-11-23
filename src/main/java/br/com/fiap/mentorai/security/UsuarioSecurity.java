package br.com.fiap.mentorai.security;

import br.com.fiap.mentorai.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component("usuarioSecurity") // Nomeia o bean para uso no @PreAuthorize
public class UsuarioSecurity {

    private final UsuarioRepository usuarioRepository;

    public UsuarioSecurity(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Verifica se o ID requisitado na URL pertence ao usuário logado (Propriedade).
     *
     * @param idRequisitado O ID da URL (e.g., {id} em /usuarios/{id}).
     * @return true se o ID for do usuário logado ou se o usuário logado for ADMIN.
     */
    public boolean isOwner(UUID idRequisitado) {
        // 1. Pega o objeto de autenticação (Principal)
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        // 2. [OPCIONAL] Se houver um ADMIN role, ele pode acessar tudo.
        // if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
        //     return true;
        // }

        // 3. Pega o e-mail (username) do usuário logado a partir do token
        String emailLogado = authentication.getName();

        // 4. Busca o ID do usuário logado e compara com o ID da URL
        return usuarioRepository.findByEmail(emailLogado)
                .map(u -> u.getId().equals(idRequisitado)) // Compara o ID logado com o ID da URL
                .orElse(false); // Retorna false se o usuário logado não for encontrado
    }
}