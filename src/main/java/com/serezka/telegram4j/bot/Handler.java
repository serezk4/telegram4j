package com.serezka.telegram4j.bot;

import com.serezka.database.authorization.model.User;
import com.serezka.database.authorization.service.UserService;
import com.serezka.telegram4j.broker.MessageBroker;
import com.serezka.telegram4j.command.Command;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.cache2k.Cache;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.cache2k.Cache2kBuilder;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author serezk4
 * @version 1.1
 * @since 1.0
 * <p>
 * Class for handling updates
 */

@Log4j2
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class Handler {
    @Getter
    List<Command> commands;
    UserService userService;
    MessageBroker broker;

    Cache<Long, User> userCache = Cache2kBuilder.of(Long.class, User.class)
            .name("userCache")
            .entryCapacity(500)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    /**
     * Handle update
     *
     * @param update - received update
     */
    public void handle(Update update) {
        if (!(update.hasCallbackQuery() || update.hasMessage())) return;

        User user = getUser(update);

        log.info("Handling: user: {} | update: {}", user, update);

        // check sessions

        // execute command
        if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equalsIgnoreCase("/help")) {
            broker.sendMessage(update.getMessage().getChatId(), getHelp(user));
            return;
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            pickCommand(update.getMessage().getText())
                    .ifPresentOrElse(command -> command.execute(user, update),
                            () -> broker.sendMessage(update.getMessage().getChatId(), "Unknown command\nType /help for help"));
        }
    }

    /**
     * Pick command by text
     *
     * @param text - text
     * @return command
     * @see Command
     */
    private Optional<Command> pickCommand(String text) {
        return commands.stream()
                .filter(command -> command.getUsage().stream().anyMatch(text::matches))
                .findFirst();
    }

    /**
     * Get help message
     *
     * @param user - user entity
     *             <p>User needs to pick commands that he can use
     * @return help message
     */
    private String getHelp(User user) {
        return "<b>Help:</b>" + commands.stream()
                .filter(command -> command.getRequiredRole().getLvl() <= user.getRole().getLvl())
                .map(command -> String.format("%n - <b>%s</b> - %s", command.getUsage(), command.getHelp()))
                .collect(Collectors.joining());
    }

    /**
     * Get user by update or create new user
     *
     * @param update - received update
     * @return user entity
     * @see #getUser(String, Long)
     */
    private User getUser(Update update) {
        // todo fix (if callback query -> get from callback query and other stuff)
        return getUser(update.getMessage().getFrom().getUserName(), update.getMessage().getChatId());
    }

    /**
     * Get user by username and chat id or create new user
     *
     * @param username - username
     * @param chatId   - chat id
     * @return user entity
     */
    private User getUser(String username, Long chatId) {
        return userCache.computeIfAbsent(chatId, id -> userService.findByChatId(id)
                .orElseGet(() -> userService.save(User.builder()
                        .username(username).chatId(id)
                        .build())));
    }
}
