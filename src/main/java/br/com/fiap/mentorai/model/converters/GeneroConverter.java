package br.com.fiap.mentorai.model.converters;

import br.com.fiap.mentorai.model.enums.Genero;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class GeneroConverter implements AttributeConverter<Genero, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Genero genero) {
        if (genero == null) {
            return null;
        }
        return genero.getCodigo();
    }

    @Override
    public Genero convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return Genero.fromCodigo(dbData);
    }
}
