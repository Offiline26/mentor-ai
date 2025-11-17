package br.com.fiap.mentorai.controller.view;

import br.com.fiap.mentorai.repository.AreaAtuacaoRepository;
import br.com.fiap.mentorai.repository.CargoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthPagesController {

    private final CargoRepository cargoRepository;
    private final AreaAtuacaoRepository areaAtuacaoRepository;

    public AuthPagesController(CargoRepository cargoRepository,
                               AreaAtuacaoRepository areaAtuacaoRepository) {
        this.cargoRepository = cargoRepository;
        this.areaAtuacaoRepository = areaAtuacaoRepository;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";     // templates/login.html
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("cargos", cargoRepository.findAll());
        model.addAttribute("areasAtuacao", areaAtuacaoRepository.findAll());
        return "register";  // templates/register.html
    }
}
