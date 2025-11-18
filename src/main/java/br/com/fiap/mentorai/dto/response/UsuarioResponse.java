package br.com.fiap.mentorai.dto.response;

import br.com.fiap.mentorai.model.enums.Genero;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioResponse {
    private UUID id;
    private String nome;
    private String email;
    private LocalDate dataNascimento;
    private Genero genero;
    private String pais;
    private UUID idCargo;
    private String cargo;
    private UUID idAreaAtuacao;
    private String areaAtuacao;
    private LocalDateTime dataCadastro;

    // Habilidades do usuário (id + nível)
    private Set<UsuarioHabilidadeItem> habilidades;

    // Rotas do usuário (status e progresso)
    private Set<UsuarioRotaItem> rotas;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class UsuarioHabilidadeItem {
        private UUID idHabilidade;
        private String habilidade;
        private Integer nivel; // 1..5 (mantém compat c/ request)
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class UsuarioRotaItem {
        private UUID idRota;
        private String nomeRota;
        private java.math.BigDecimal progressoPercentual;
        private String status; // EM_ANDAMENTO, CONCLUIDA, ...
        private LocalDateTime dataInicio;
        private LocalDateTime dataConclusao;
    }
}
