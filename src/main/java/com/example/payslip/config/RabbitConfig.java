package com.example.payslip.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String BROKER_NOTE_INFO_QUEUE = "broker.note.info.queue";

    @Bean
    public Queue brokerNoteInfoQueue() {
        return new Queue(BROKER_NOTE_INFO_QUEUE, true);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
