package br.com.fiap.mentorai.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_MENTORAI = "mentorai.exchange";
    public static final String QUEUE_ROTA_CRIADA = "mentorai.rota.criada";
    public static final String ROUTING_KEY_ROTA_CRIADA = "rota.criada";

    @Bean
    public DirectExchange mentorAiExchange() {
        return new DirectExchange(EXCHANGE_MENTORAI);
    }

    @Bean
    public Queue rotaCriadaQueue() {
        return new Queue(QUEUE_ROTA_CRIADA, true);
    }

    @Bean
    public Binding rotaCriadaBinding(Queue rotaCriadaQueue, DirectExchange mentorAiExchange) {
        return BindingBuilder
                .bind(rotaCriadaQueue)
                .to(mentorAiExchange)
                .with(ROUTING_KEY_ROTA_CRIADA);
    }
}
