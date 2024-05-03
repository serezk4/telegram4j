package com.serezka.telegram4j.session.menu;

import com.serezka.database.authorization.model.User;
import com.serezka.telegram4j.keyboard.Keyboard;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.function.TriFunction;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;

/**
 * @author serezk4
 * @version 1.0
 * @see MenuSession
 * @since 1.12
 * <p>
 * Class for menu page
 */

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Getter
public class Page {
    InputMedia inputMedia;
    String text;
    Keyboard keyboard;

    public Page(String text, Keyboard keyboard) {
        this(null, text, keyboard);
    }

    public Page(String text) {
        this(text, null);
    }

    // todo fix shitcode

    /**
     * Interface for page generation
     */
    public interface Generator {
        Page apply(MenuSession session, User user, Update update);
    }

    /**
     * Class for page generation by function
     *
     * @param func - function for page generation
     */
    public record GenerateByFunction(TriFunction<MenuSession, User, Update, Page> func) implements Generator {
        @Override
        public Page apply(MenuSession session, User user, Update update) {
            return func.apply(session, user, update);
        }
    }

    /**
     * Class for static page generation
     */
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @AllArgsConstructor
    public static final class Static implements Generator {
        Page page;

        @Override
        public Page apply(MenuSession session, User user, Update update) {
            return page;
        }
    }
}
