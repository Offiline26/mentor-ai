package br.com.fiap.mentorai.model.enums;

public enum NivelProficienciaEnum {

    INICIANTE(1),
    BASICO(2),
    INTERMEDIARIO(3),
    AVANCADO(4),
    ESPECIALISTA(5);

    private final int valorDb;

    NivelProficienciaEnum(int valorDb) {
        this.valorDb = valorDb;
    }

    public int getValorDb() {
        return valorDb;
    }

    public static NivelProficienciaEnum fromValorDb(Integer valorDb) {
        if (valorDb == null) {
            return null;
        }
        for (NivelProficienciaEnum nivel : values()) {
            if (nivel.valorDb == valorDb) {
                return nivel;
            }
        }
        throw new IllegalArgumentException("Nível de proficiência inválido: " + valorDb);
    }
}
