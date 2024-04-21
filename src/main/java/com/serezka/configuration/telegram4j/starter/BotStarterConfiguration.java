package com.serezka.configuration.telegram4j.starter;

import com.serezka.telegram4j.bot.Bot;
import com.serezka.telegram4j.starter.BotStarter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotStarterConfiguration {
    @Bean(initMethod = "start")
    public BotStarter botStarter(Bot bot) {
        return new BotStarter(bot);
    }
}
