package br.com.fiap.mentorai.model.enums;

public enum GeneroEnum {

    MASCULINO(1),
    FEMININO(2),
    NAO_BINARIO(3),
    PREFIRO_NAO_INFORMAR(4);

    private final int idDb;

    GeneroEnum(int idDb) {
        this.idDb = idDb;
    }

    public int getIdDb() {
        return idDb;
    }

    public static GeneroEnum fromIdDb(Integer idDb) {
        if (idDb == null) {
            return null;
        }
        for (GeneroEnum genero : values()) {
            if (genero.idDb == idDb) {
                return genero;
            }
        }
        throw new IllegalArgumentException("Id de gênero inválido: " + idDb);
    }
}
