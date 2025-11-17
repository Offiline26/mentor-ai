package br.com.fiap.mentorai.dto.ai;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RecomendacaoRotaResponse {
    private String resumoEstrategia;
    private List<String> passosSugeridos;
    private List<String> habilidadesPrioritarias;
    private List<String> sugestoesDeCursos;    // títulos/textos livres
}

