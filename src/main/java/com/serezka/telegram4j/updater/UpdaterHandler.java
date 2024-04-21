package com.serezka.telegram4j.updater;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author serezk4
 * @version 1.0
 * @since 1.0
 * <p>
 * Class for handling updaters
 */

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PropertySource("classpath:telegram.properties")
@ComponentScan("com.serezka.telegram.updater")
@Log4j2
public class UpdaterHandler {
    ScheduledExecutorService executorService;

    public UpdaterHandler(@Value("${telegram.updaters.threads}") int threads, List<Updater> updaters) {
        this.executorService = Executors.newScheduledThreadPool(threads);

        updaters.forEach(updater -> {
            log.info("registering updater: {} | initdelay: {} | delay: {} | timeunit: {}", updater.getClass().getSimpleName(),
                    updater.getInitDelay(), updater.getDelay(), updater.getUnit());
            executorService.scheduleAtFixedRate(updater, updater.getInitDelay(), updater.getDelay(), updater.getUnit());
        });
    }
}

