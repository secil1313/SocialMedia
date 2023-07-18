package com.emre.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    //create post producer işlemi için gerekli değişkeler
    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.queueCreatePost}")
    private String queueCreatePost;
    @Value("${rabbitmq.bindingKeyCreatePost}")
    private String createPostBindingKey;

    @Bean
    Queue queueCreatePost() {return new Queue(queueCreatePost);}
    @Bean
    DirectExchange exchange(){return new DirectExchange(exchange);}
    @Bean
    public Binding createPostBindingKey(final Queue queueCreatePost,final DirectExchange exchange) {
        return BindingBuilder.bind(queueCreatePost).to(exchange).with(createPostBindingKey);
    }
}
