package com.serezka.telegram4j.keyboard;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Keyboard {
    public static class Delimiter {
        public static final String SERVICE = "$";
        public static final String DATA = "*";
    }

    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @Getter
    @AllArgsConstructor
    public enum Actions {
        CLOSE("\uD83E\uDD0F Закрыть", "exit"), BACK("назад", "back");

        String name;
        String callback;
    }
}