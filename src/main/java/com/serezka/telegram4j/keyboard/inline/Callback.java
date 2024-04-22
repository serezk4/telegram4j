package com.serezka.telegram4j.keyboard.inline;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author serezk4
 * @version 1.0
 * @since 1.11
 * @status in development
 * <p>
 * Class for inline keyboard callback
 */

@Log4j2
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Builder
public class Callback {
    String link;
    List<String> data;

    @NonFinal @Getter @Setter
    Update update;

    /**
     * From callback to string
     * @return callback in string representation
     */
    public String toCallback() {
        return  link + Delimiter.SERVICE + data.stream().map(Object::toString).reduce((a, b) -> a + Delimiter.DATA + b).orElse("");
    }

    /**
     * From string to callback
     * @param raw - string representation of callback
     * @return callback
     */
    public static Callback fromCallback(String raw) {
        String[] args = raw.split(Delimiter.SERVICE, 2);

        if (args.length > 2) log.warn("Callback has more than 2 parts, ignoring the rest");
        if (args.length < 2) log.warn("Callback has less than 2 parts, the second part will be empty");
        if (args.length == 0) {
            log.warn("Callback has no parts, returning empty list");
            return Callback.empty();
        }

        String link = args[0];
        List<String> data = new ArrayList<>(args.length > 1 ? Arrays.stream(args[1].split(Delimiter.DATA)).toList() : Collections.emptyList());

        return new Callback(link, data);
    }

    public static Callback empty() {
        return new Callback("", Collections.emptyList());
    }

    public static class Delimiter {
        public static final String SERVICE = ":";
        public static final String DATA = "%";
    }
}
