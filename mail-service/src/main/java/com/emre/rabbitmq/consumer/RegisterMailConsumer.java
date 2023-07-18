package com.emre.rabbitmq.consumer;

import com.emre.rabbitmq.model.RegisterMailModel;
import com.emre.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterMailConsumer {
    private final MailService mailService;

    @RabbitListener(queues = ("${rabbitmq.registerMailQueue}"))
    public void sendActivationCode(RegisterMailModel model) {
        mailService.createMailWithRabbitMq(model);
    }

}
