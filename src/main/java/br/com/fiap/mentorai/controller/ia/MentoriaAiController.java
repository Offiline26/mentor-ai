package br.com.fiap.mentorai.controller.ia;

import br.com.fiap.mentorai.dto.ai.RecomendacaoRotaRequest;
import br.com.fiap.mentorai.dto.ai.RecomendacaoRotaResponse;
import br.com.fiap.mentorai.service.ai.MentoriaAiService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ia/mentor")
public class MentoriaAiController {

    private final MentoriaAiService mentoriaAiService;

    public MentoriaAiController(MentoriaAiService mentoriaAiService) {
        this.mentoriaAiService = mentoriaAiService;
    }

    @PostMapping("/recomendacoes")
    public ResponseEntity<RecomendacaoRotaResponse> recomendarRota(
            @Valid @RequestBody RecomendacaoRotaRequest req) {
        RecomendacaoRotaResponse resp = mentoriaAiService.sugerirRota(req);
        return ResponseEntity.ok(resp);
    }
}
