package br.com.fiap.mentorai.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthPagesController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";     // -> templates/login.html
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";  // -> templates/register.html
    }
}
