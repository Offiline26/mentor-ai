package br.com.fiap.mentorai.controller.view;

import br.com.fiap.mentorai.dto.ai.RecomendacaoRotaRequest;
import br.com.fiap.mentorai.dto.ai.RecomendacaoRotaResponse;
import br.com.fiap.mentorai.service.ai.MentoriaAiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/mentor-test")
public class MentorWebController {

    private final MentoriaAiService aiService;

    public MentorWebController(MentoriaAiService aiService) {
        this.aiService = aiService;
    }

    @GetMapping
    public String showForm(Model model) {
        // Objeto vazio para o form
        model.addAttribute("request", new RecomendacaoRotaRequest());
        return "mentor-test"; // Nome do arquivo HTML
    }

    @PostMapping
    public String gerarMentoria(@ModelAttribute RecomendacaoRotaRequest req, Model model) {
        try {
            // Processamento da IA (Pode demorar 30-60s)
            RecomendacaoRotaResponse response = aiService.sugerirRota(req);

            model.addAttribute("resultado", response);
            model.addAttribute("request", req); // Mantém os dados preenchidos
            model.addAttribute("sucesso", "Mentoria gerada com sucesso via Ollama Phi-3!");

        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao processar IA: " + e.getMessage());
            model.addAttribute("request", req);
        }
        return "mentor-test";
    }
}
