package br.com.fiap.mentorai.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Genero {
    MASCULINO(1),
    FEMININO(2),
    OUTRO(3),
    PREFIRO_NAO_INFORMAR(4);

    private final int codigo;

    Genero(int codigo) {
        this.codigo = codigo;
    }

    @JsonValue // Serializa para o Front como número (ex: 2) ou use @JsonValue no name() se quiser string
    public int getCodigo() {
        return codigo;
    }

    // Permite que o JSON de entrada aceite tanto o número quanto a String "FEMININO"
    @JsonCreator
    public static Genero fromValue(Object value) {
        if (value instanceof Integer) {
            return fromCodigo((Integer) value);
        }
        if (value instanceof String) {
            try {
                return Genero.valueOf(((String) value).toUpperCase());
            } catch (IllegalArgumentException e) {
                // Tenta parsear string numérica "1"
                try {
                    return fromCodigo(Integer.parseInt((String) value));
                } catch (NumberFormatException ex) {
                    return null;
                }
            }
        }
        return null;
    }

    public static Genero fromCodigo(Integer codigo) {
        if (codigo == null) return null;
        for (Genero g : values()) {
            if (g.codigo == codigo) return g;
        }
        throw new IllegalArgumentException("Código de gênero inválido: " + codigo);
    }
}
