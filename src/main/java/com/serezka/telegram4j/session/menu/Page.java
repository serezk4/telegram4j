package com.serezka.telegram4j.session.menu;

import com.serezka.database.authorization.model.User;
import com.serezka.telegram4j.keyboard.Button;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.function.TriFunction;
import org.hibernate.sql.Update;

import java.util.Collections;
import java.util.List;

/**
 * @author serezk4
 * @version 1.0
 * @since 1.12
 * <p>
 * Class for menu page
 * @see MenuSession
 */

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class Page {
    String text;
    List<Button.Inline> buttons;
    int rowSize;

    public Page(String text, List<Button.Inline> buttons) {
        this(text, buttons, 2);
    }

    public Page(String text) {
        this(text, Collections.emptyList());
    }

    // todo fix shitcode

    /**
     * Interface for page generation
     */
    public interface Generatable {
        Page apply(MenuSession session, User user, Update update);
    }

    /**
     * Class for page generation by function
     * @param func - function for page generation
     */
    public record Generator(TriFunction<MenuSession, User, Update, Page> func) implements Generatable {
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
    public static final class Static implements Generatable {
        Page page;

        @Override
        public Page apply(MenuSession session, User user, Update update) {
            return page;
        }
    }


}
