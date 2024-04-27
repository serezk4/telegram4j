package com.serezka.telegram4j.command;

import com.serezka.database.authorization.model.User;
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
 * Abstract class for command
 * Create your own command by extending this class
 */

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@RequiredArgsConstructor
public abstract class Command {
    List<String> usage; // * in regex
    String help;
    User.Role requiredRole;

    /**
     * Execute command
     * @param user - user entity
     * @param update - received update
     */
    public abstract void execute(User user, Update update);
}
