package br.com.fiap.mentorai.messaging;

import br.com.fiap.mentorai.config.RabbitConfig;
import br.com.fiap.mentorai.messaging.RotaCriadaEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RotaEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RotaEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishRotaCriada(RotaCriadaEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_MENTORAI,
                RabbitConfig.ROUTING_KEY_ROTA_CRIADA,
                event
        );
    }
}
