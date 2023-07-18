package com.emre.rabbitmq.producer;

import com.emre.rabbitmq.model.RegisterModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterProducer {
    @Value("${rabbitmq.exchange-auth}")
    private String exchange;
    @Value("${rabbitmq.registerKey}")
    private String registerBindingKey;
    //Bu template üzerinden, dışarıdan gelen veri kuyruğa eklenir.
    private final RabbitTemplate rabbitTemplate;
    public void sendNewUser(RegisterModel registerModel) {
        rabbitTemplate.convertAndSend(exchange,registerBindingKey,registerModel);
    }
}
