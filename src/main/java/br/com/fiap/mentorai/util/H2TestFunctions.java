package br.com.fiap.mentorai.util;

import java.util.regex.Pattern;

public class H2TestFunctions {

    // Regex padrão de e-mail (simulando a lógica do Oracle)
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern PATTERN = Pattern.compile(EMAIL_PATTERN);

    /**
     * Método estático que será chamado pelo H2.
     * A assinatura deve bater com a chamada SQL.
     */
    public static Integer validarEmailFormato(String email) {
        if (email == null) return 0;
        return PATTERN.matcher(email).matches() ? 1 : 0;
    }
}
