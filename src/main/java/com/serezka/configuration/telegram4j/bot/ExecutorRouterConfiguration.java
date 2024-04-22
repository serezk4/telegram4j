package com.serezka.configuration.telegram4j.bot;

import com.serezka.telegram4j.bot.ExecutorRouter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author serezk4
 * @version 1.0
 * @since 1.0
 * <p>
 * Configuration class for executor router
 * @see ExecutorRouter
 */

@Configuration
@PropertySource("classpath:telegram.properties")
public class ExecutorRouterConfiguration {
    /**
     * Bean for executor router
     * Creates ExecutorRouter with specified count of threads
     * @param threadsCount Count of threads for executor
     * @return ExecutorRouter
     */
    @Bean
    public ExecutorRouter executorRouter(@Value("${telegram.bot.threads}") int threadsCount) {
        return new ExecutorRouter(threadsCount);
    }
}
