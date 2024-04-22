package com.serezka.telegram4j.keyboard;

import com.serezka.telegram4j.keyboard.inline.Callback;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;

/**
 * @author serezk4
 * @version 1.0
 * @since 1.11
 * <p>
 * Class for buttons
 */

public class Button {

    /**
     * Class for reply keyboard button
     *
     * @version 1.0
     * @since 1.11
     */

    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class Reply {
        String text;
        WebAppInfo webAppInfo;

        public Reply(String text) {
            this.text = text;
            this.webAppInfo = null;
        }
    }

    /**
     * Class for inline keyboard button
     *
     * @version 1.0
     * @since 1.11
     */

    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class Inline {
        String text;
        Callback callback;
        WebAppInfo webAppInfo;

        public Inline(String text, Callback callback) {
            this.text = text;
            this.callback = callback;
            this.webAppInfo = null;
        }
    }
}
