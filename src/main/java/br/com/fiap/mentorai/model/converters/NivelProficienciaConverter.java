package br.com.fiap.mentorai.model.converters;

import br.com.fiap.mentorai.model.enums.NivelProficienciaEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class NivelProficienciaConverter implements AttributeConverter<NivelProficienciaEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(NivelProficienciaEnum atributo) {
        return (atributo != null) ? atributo.getValorDb() : null;
    }

    @Override
    public NivelProficienciaEnum convertToEntityAttribute(Integer dbData) {
        return NivelProficienciaEnum.fromValorDb(dbData);
    }
}
