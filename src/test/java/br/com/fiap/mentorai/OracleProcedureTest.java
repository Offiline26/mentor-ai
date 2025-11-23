package br.com.fiap.mentorai;

import br.com.fiap.mentorai.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
// 1. Define que vamos usar o arquivo 'application-oracle-test.properties'
// 2. IMPEDE que o Spring troque o Oracle pelo H2 em memória
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OracleProcedureTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("INTEGRAÇÃO REAL: Deve retornar 1 (Válido) do Oracle")
    void testEmailValidoNoOracle() {
        // Cenário
        String email = "teste.oracle@fiap.com.br";

        // Ação (Chama a Procedure Real no Banco)
        Integer resultado = usuarioRepository.validarEmailFormato(email);

        // Validação
        System.out.println(">>> RETORNO DO ORACLE: " + resultado);
        assertEquals(1, resultado, "Oracle deve validar o email como correto");
    }

    @Test
    @DisplayName("INTEGRAÇÃO REAL: Deve retornar 0 (Inválido) do Oracle")
    void testEmailInvallidoNoOracle() {
        // Cenário
        String email = "email_sem_arroba";

        // Ação
        Integer resultado = usuarioRepository.validarEmailFormato(email);

        // Validação
        System.out.println(">>> RETORNO DO ORACLE: " + resultado);
        assertEquals(0, resultado, "Oracle deve invalidar o email");
    }
}
