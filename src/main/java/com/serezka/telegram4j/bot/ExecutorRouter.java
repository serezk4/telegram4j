package com.serezka.telegram4j.bot;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * @author serezk4
 * @version 1.0
 * @since 1.0
 * <p>
 * Router for executor services
 * Helps load balancing
 */

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
public class ExecutorRouter {
    int threadsCount;
    List<ExecutorService> services;

    @NonFinal boolean shutdown = false;

    public ExecutorRouter(int threadsCount) {
        services = new ArrayList<>(threadsCount);
        IntStream.range(0, threadsCount).mapToObj(i -> Executors.newSingleThreadExecutor()).forEach(services::add);

        this.threadsCount = threadsCount;

        log.info("created executor service router with {} services", this.threadsCount);
    }

    /**
     * Check if all services are shut down
     *
     * @return true if all services are shut down
     */
    public boolean isShutdown() {
        return !(!shutdown || !(shutdown = services.stream().allMatch(ExecutorService::isShutdown)));
    }

    /**
     * Route task to executor
     *
     * @param id       task id
     * @param runnable task
     */
    public void route(long id, Runnable runnable) {
        log.info("sent task#{} to executor#{}", id, (int) ((id) % threadsCount));
        services.get((int) ((id) % threadsCount)).execute(runnable);
    }

    /**
     * Shut down all services
     */
    public void shutdown() {
        log.info("shutting down...");
        shutdown = true;
        services.forEach(ExecutorService::shutdown);
        log.info("turned off successfully");
    }
}
