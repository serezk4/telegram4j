package com.serezka.configuration.telegram4j.bot;

import com.serezka.telegram4j.bot.ExecutorRouter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:telegram.properties")
public class ExecutorRouterConfiguration {
    @Bean
    public ExecutorRouter executorRouter(@Value("${telegram.bot.threads}") int threadsCount) {
        return new ExecutorRouter(threadsCount);
    }
}
