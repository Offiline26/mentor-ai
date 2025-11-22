package br.com.fiap.mentorai.model.converters;

import br.com.fiap.mentorai.model.enums.Genero;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

// Aplica o conversor automaticamente para todas as entidades que usam o tipo Genero
@Converter(autoApply = true)
public class GeneroConverter implements AttributeConverter<Genero, Integer> {

    // Método que converte o objeto Java (Genero) para o tipo do Banco de Dados (Integer)
    @Override
    public Integer convertToDatabaseColumn(Genero genero) {
        if (genero == null) {
            return null; // Trata NULLs
        }
        // Retorna o índice (ordinal) do Enum: MASCULINO=0, FEMININO=1, etc.
        return genero.ordinal();
    }

    // Método que converte o valor do Banco de Dados (Integer) para o objeto Java (Genero)
    @Override
    public Genero convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null; // Trata NULLs
        }

        // Busca o Enum pelo índice (ordinal)
        return Stream.of(Genero.values())
                .filter(g -> g.ordinal() == dbData)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Código de Gênero inválido: " + dbData));
    }
}