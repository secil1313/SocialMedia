package com.emre.rabbitmq.producer;

import com.emre.rabbitmq.model.RegisterElasticModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterElasticProducer {
    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.elasticRegisterBindingKey}")
    private String elasticRegisterBindingKey;

    private final RabbitTemplate rabbitTemplate;

    public void sendNewUser(RegisterElasticModel registerElasticModel){
        rabbitTemplate.convertAndSend(exchange, elasticRegisterBindingKey, registerElasticModel);
    }

}
