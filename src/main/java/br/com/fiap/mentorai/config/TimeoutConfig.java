package br.com.fiap.mentorai.config;

import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

@Configuration
public class TimeoutConfig {

    @Bean
    public RestClientCustomizer restClientCustomizer() {
        return builder -> builder.requestFactory(new SimpleClientHttpRequestFactory() {{
            // Define timeout de 3 minutos (180 segundos)
            // Isso se aplica a todos os RestClients gerenciados pelo Spring
            setConnectTimeout(180 * 1000);
            setReadTimeout(180 * 1000);
        }});
    }
}
