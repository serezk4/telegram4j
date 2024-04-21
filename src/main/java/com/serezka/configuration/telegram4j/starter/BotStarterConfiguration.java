package com.serezka.configuration.telegram4j.starter;

import com.serezka.telegram4j.bot.Bot;
import com.serezka.telegram4j.starter.BotStarter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:telegram.properties")
public class BotStarterConfiguration {
    @Bean(initMethod = "start")
    public BotStarter botStarter(@Value("${telegram.bot.token}") String token, Bot bot) {
        return new BotStarter(token, bot);
    }
}
