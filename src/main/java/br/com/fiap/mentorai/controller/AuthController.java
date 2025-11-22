package br.com.fiap.mentorai.controller;

import br.com.fiap.mentorai.dto.auth.LoginRequest;
import br.com.fiap.mentorai.dto.auth.LoginResponse;
import br.com.fiap.mentorai.dto.auth.RegisterRequest;
import br.com.fiap.mentorai.dto.auth.RegisterResponse;
import br.com.fiap.mentorai.exception.ResourceNotFoundException;
import br.com.fiap.mentorai.model.AreaAtuacao;
import br.com.fiap.mentorai.model.Cargo;
import br.com.fiap.mentorai.model.Usuario;
import br.com.fiap.mentorai.repository.AreaAtuacaoRepository;
import br.com.fiap.mentorai.repository.CargoRepository;
import br.com.fiap.mentorai.repository.UsuarioRepository;
import br.com.fiap.mentorai.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final CargoRepository cargoRepository;
    private final AreaAtuacaoRepository areaAtuacaoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager,
                          UsuarioRepository usuarioRepository,
                          CargoRepository cargoRepository,
                          AreaAtuacaoRepository areaAtuacaoRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService) {

        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.cargoRepository = cargoRepository;
        this.areaAtuacaoRepository = areaAtuacaoRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // ------------------------------
    //           LOGIN
    // ------------------------------
    // ------------------------------
    //           LOGIN (COM DEBUG)
    // ------------------------------
    @PostMapping("/login")
    public ResponseEntity<?> apiLogin(@Valid @RequestBody LoginRequest req) {

        // --- DEBUG DE ARQUITETO ---
        String emailRecebido = req.getEmail();
        System.out.println("========================================");
        System.out.println(">>> [DEBUG LOGIN] Recebido do Front: '" + emailRecebido + "'");

        if (emailRecebido != null) {
            System.out.println(">>> [DEBUG LOGIN] Tamanho: " + emailRecebido.length());
            System.out.println(">>> [DEBUG LOGIN] ASCII Codes: ");
            // Isso vai imprimir os números de cada letra.
            // Se tiver espaço fantasma, vai aparecer um número estranho (não 32, que é espaço normal)
            req.getEmail().chars().forEach(c -> System.out.print(c + " "));
            System.out.println("\n========================================");
        }
        // ---------------------------

        try {
            // Força limpeza agressiva (remove tudo que não é visível)
            // O replaceAll abaixo remove caracteres de controle unicode invisíveis
            final String emailNormalizado = req.getEmail()
                    .replaceAll("\\p{C}", "")
                    .trim()
                    .toLowerCase(Locale.ROOT);

            System.out.println(">>> [DEBUG LOGIN] Usado na busca: '" + emailNormalizado + "'");

            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(emailNormalizado, req.getSenha())
            );

            UserDetails principal = (UserDetails) auth.getPrincipal();
            String token = jwtService.generate(principal.getUsername(), principal.getAuthorities());

            Usuario u = usuarioRepository.findByEmail(emailNormalizado)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado (Pós-Auth)"));

            LoginResponse userInfo = LoginResponse.builder()
                    .idUsuario(u.getId())
                    .nome(u.getNome())
                    .email(u.getEmail())
                    .build();

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("accessToken", token);
            body.put("tokenType", "Bearer");
            body.put("user", userInfo);

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .body(body);

        } catch (BadCredentialsException e) {
            System.out.println(">>> [DEBUG LOGIN] Falha: BadCredentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "E-mail ou senha inválidos"));
        } catch (Exception e) {
            System.out.println(">>> [DEBUG LOGIN] Erro Genérico: " + e.getMessage());
            e.printStackTrace(); // Veja o stacktrace no console
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ------------------------------
    //           REGISTER
    // ------------------------------
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {

        String email = req.getEmail().trim().toLowerCase(Locale.ROOT);

        if (usuarioRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "E-mail já cadastrado"));
        }

        Cargo cargo = cargoRepository.findById(req.getIdCargo())
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado"));

        AreaAtuacao area = areaAtuacaoRepository.findById(req.getIdAreaAtuacao())
                .orElseThrow(() -> new ResourceNotFoundException("Área de atuação não encontrada"));

        Usuario u = new Usuario();
        u.setNome(req.getNome());
        u.setEmail(email);
        u.setSenha(passwordEncoder.encode(req.getSenha()));
        u.setDataNascimento(req.getDataNascimento());
        u.setGenero(req.getGenero());
        u.setPais(req.getPais());
        u.setCargo(cargo);
        u.setAreaAtuacao(area);
        u.setDataCadastro(LocalDateTime.now());

        u = usuarioRepository.save(u);

        RegisterResponse resp = RegisterResponse.builder()
                .idUsuario(u.getId())
                .nome(u.getNome())
                .email(u.getEmail())
                .dataNascimento(u.getDataNascimento())
                .genero(u.getGenero())
                .pais(u.getPais())
                .idCargo(cargo.getId())
                .cargo(cargo.getNome())          // ajusta pro nome real do campo em Cargo
                .idAreaAtuacao(area.getId())
                .areaAtuacao(area.getNome())     // idem para AreaAtuacao
                .dataCadastro(u.getDataCadastro())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    // ------------------------------
    //             ME
    // ------------------------------
    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal UserDetails principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Não autenticado"));
        }

        Usuario u = usuarioRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        LoginResponse response = LoginResponse.builder()
                .idUsuario(u.getId())
                .nome(u.getNome())
                .email(u.getEmail())
                .build();

        return ResponseEntity.ok(response);
    }
}