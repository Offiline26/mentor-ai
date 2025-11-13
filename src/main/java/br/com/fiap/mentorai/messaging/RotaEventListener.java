package br.com.fiap.mentorai.messaging;

import br.com.fiap.mentorai.config.RabbitConfig;
import br.com.fiap.mentorai.messaging.RotaCriadaEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RotaEventListener {

    @RabbitListener(queues = RabbitConfig.QUEUE_ROTA_CRIADA)
    public void onRotaCriada(RotaCriadaEvent event) {
        // Aqui você simula algo assíncrono: envio de e-mail, notificação, log inteligente, etc.
        log.info("📩 Evento de rota criada recebido: id={}, nome={}, geradaPorIa={}",
                event.getIdRota(),
                event.getNomeRota(),
                event.getGeradaPorIa());

        // Pra demo você pode falar:
        // "Aqui poderíamos mandar um e-mail para o usuário com a trilha de requalificação sugerida."
    }
}