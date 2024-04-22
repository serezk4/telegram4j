package com.serezka.telegram4j.bot;

import com.serezka.database.authorization.model.User;
import com.serezka.database.authorization.service.UserService;
import com.serezka.telegram4j.command.Command;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.cache2k.Cache;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.cache2k.Cache2kBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

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
    UserService userService;

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
        User user = getUser(update);
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
                .orElseGet(() -> User.builder()
                        .username(username).chatId(id)
                        .build()));
    }
}
