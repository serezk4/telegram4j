package com.serezka.telegram4j.session.notify;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author serezk4
 * @version 1.0
 * @since 1.13
 * <p>
 * Class for notification
 */

public record Notification(String message, Type type) {

    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @RequiredArgsConstructor
    @Getter
    public enum Type {
        INFO("ℹ️"),
        WARNING("⚠️"),
        ERROR("❌");

        String emoji;
    }
}


