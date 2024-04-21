package com.serezka.telegram4j.bot;

import com.serezka.telegram4j.command.Command;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * @author serezk4
 * @version 1.0
 * @since 1.0
 * <p>
 * Class for handling updates
 */

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class Handler {
    @Getter
    List<Command> commands;

    /**
     * Handle update
     *
     * @param update - received update
     */
    public void handle(Update update) {
        // todo
    }
}
