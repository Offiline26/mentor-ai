package br.com.fiap.mentorai.model.converters;

import br.com.fiap.mentorai.model.enums.GeneroEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class GeneroEnumConverter implements AttributeConverter<GeneroEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(GeneroEnum genero) {
        return (genero != null) ? genero.getIdDb() : null;
    }

    @Override
    public GeneroEnum convertToEntityAttribute(Integer dbData) {
        return GeneroEnum.fromIdDb(dbData);
    }
}
