package br.com.fiap.mentorai.service.ai;

import br.com.fiap.mentorai.dto.ai.RecomendacaoRotaRequest;
import br.com.fiap.mentorai.dto.ai.RecomendacaoRotaResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MentoriaAiService {

    private final ChatClient chatClient;

    public MentoriaAiService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public RecomendacaoRotaResponse sugerirRota(RecomendacaoRotaRequest req) {

        String promptText = """
                Você é um mentor de carreira especializado em requalificação profissional,
                focado em tecnologia e profissões digitais.

                Considere os dados abaixo:

                - Nome do usuário: %s
                - Objetivo profissional: %s
                - Cargo atual: %s
                - Área de atuação atual: %s
                - Habilidades atuais: %s
                - Habilidades desejadas: %s
                - Horas disponíveis por semana para estudo: %s

                Gere uma resposta em português, com a seguinte estrutura:

                [RESUMO]
                (um parágrafo curto com a estratégia geral de requalificação)

                [PASSOS]
                - passo 1
                - passo 2
                - passo 3
                (liste de 3 a 7 passos práticos e objetivos)

                [HABILIDADES PRIORITÁRIAS]
                - habilidade 1
                - habilidade 2
                - habilidade 3
                (liste de 3 a 10 habilidades que a pessoa deveria priorizar)

                [SUGESTÕES DE CURSOS]
                - tema de curso 1
                - tema de curso 2
                - tema de curso 3
                (não cite plataformas específicas, só os temas/assuntos)
                """
                .formatted(
                        nullSafe(req.getNomeUsuario()),
                        nullSafe(req.getObjetivoProfissional()),
                        nullSafe(req.getCargoAtual()),
                        nullSafe(req.getAreaAtuacao()),
                        String.join(", ", nonNullList(req.getHabilidadesAtuais())),
                        String.join(", ", nonNullList(req.getHabilidadesDesejadas())),
                        req.getHorasPorSemana() == null ? "não informado" : req.getHorasPorSemana() + " horas"
                );

        String content = chatClient
                .prompt()
                .user(promptText)
                .call()
                .content();

        // parse igual você já estava fazendo
        List<String> linhas = Arrays.asList(content.split("\\r?\\n"));

        String resumo = extrairSecao(linhas, "[RESUMO]");
        List<String> passos = extrairLista(linhas, "[PASSOS]", "[HABILIDADES PRIORITÁRIAS]");
        List<String> habilidades = extrairLista(linhas, "[HABILIDADES PRIORITÁRIAS]", "[SUGESTÕES DE CURSOS]");
        List<String> cursos = extrairLista(linhas, "[SUGESTÕES DE CURSOS]", null);

        return RecomendacaoRotaResponse.builder()
                .resumoEstrategia(resumo)
                .passosSugeridos(passos)
                .habilidadesPrioritarias(habilidades)
                .sugestoesDeCursos(cursos)
                .build();
    }

    private static String nullSafe(String s) {
        return s == null ? "não informado" : s;
    }

    private static List<String> nonNullList(List<String> list) {
        return list == null ? List.of() : list;
    }

    private static String extrairSecao(List<String> linhas, String marcador) {
        StringBuilder sb = new StringBuilder();
        boolean dentro = false;

        for (String linha : linhas) {
            String t = linha.trim();

            if (t.equalsIgnoreCase(marcador)) {
                dentro = true;
                continue;
            }

            // próxima seção: começa com "[" → paramos
            if (dentro && t.startsWith("[")) {
                break;
            }

            if (dentro) {
                sb.append(linha).append('\n');
            }
        }

        return sb.toString().trim();
    }

    private static List<String> extrairLista(List<String> linhas, String inicio, String fim) {
        List<String> resultado = new ArrayList<>();
        boolean dentro = false;

        for (String linha : linhas) {
            String t = linha.trim();

            if (t.equalsIgnoreCase(inicio)) {
                dentro = true;
                continue;
            }

            if (fim != null && t.equalsIgnoreCase(fim)) {
                // chegamos na próxima seção, paramos
                dentro = false;
                break;
            }

            if (dentro && t.startsWith("-")) {
                // remove o "- " do começo
                String item = t.replaceFirst("^-\\s*", "").trim();
                if (!item.isEmpty()) {
                    resultado.add(item);
                }
            }
        }

        return resultado;
    }
}
