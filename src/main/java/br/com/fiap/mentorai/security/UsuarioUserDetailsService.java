package br.com.fiap.mentorai.security;

import br.com.fiap.mentorai.model.Usuario;
import br.com.fiap.mentorai.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class UsuarioUserDetailsService implements UserDetailsService {

    private final UsuarioRepository repo;

    public UsuarioUserDetailsService(UsuarioRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // CORREÇÃO: Aplica a normalização (trim e toLowerCase) no username recebido
        final String emailNormalizado = username.trim().toLowerCase(Locale.ROOT);

        Usuario u = repo.findByEmail(emailNormalizado)
                .orElseThrow(() -> new UsernameNotFoundException("E-mail não encontrado"));

        // Por enquanto todo mundo é ROLE_USER. Depois dá pra mapear Cargo → ROLE_XXX.
        var auth = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        return new org.springframework.security.core.userdetails.User(
                u.getEmail(),
                u.getSenha(), // Já é o hash BCrypt
                auth
        );
    }
}