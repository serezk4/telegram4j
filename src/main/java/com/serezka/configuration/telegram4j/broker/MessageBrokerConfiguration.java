package com.serezka.configuration.telegram4j.broker;

import com.serezka.telegram4j.broker.MessageBroker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageBrokerConfiguration {
    @Bean
    public MessageBroker messageBroker() {
        return new MessageBroker(); // todo add localization and bot instance
    }
}
