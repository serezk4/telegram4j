package com.serezka.telegram4j.keyboard.inline;

import com.serezka.telegram4j.keyboard.Button;
import com.serezka.telegram4j.keyboard.Keyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.*;
import java.util.stream.IntStream;

/**
 * @author serezk4
 * @version 1.0
 * <p>
 * Util class for inline keyboard
 * @since 1.11
 */

public class Inline {

    /**
     * Interface for inline dynamic keyboard
     * @param buttons - buttons data
     * @param rowSize - size of each row
     */
    public record DynamicKeyboard(List<Button.Inline> buttons, int rowSize) implements Keyboard {
        @Override
        public ReplyKeyboard toReplyKeyboard() {
            return Inline.getResizableKeyboard(buttons, rowSize);
        }
    }

    /**
     * Interface for inline static keyboard
     * @param buttonsData - buttons data
     */
    public record StaticKeyboard(Button.Inline[][] buttonsData) implements Keyboard {
        @Override
        public ReplyKeyboard toReplyKeyboard() {
            return Inline.getStaticKeyboard(buttonsData);
        }
    }

    /**
     * Get static inline keyboard
     *
     * @param buttonsData - buttons data
     * @return inline keyboard
     */
    public static InlineKeyboardMarkup getStaticKeyboard(Button.Inline[][] buttonsData) {
        List<InlineKeyboardRow> rows = new ArrayList<>();

        Arrays.stream(buttonsData).forEach(row ->
                rows.add(Arrays.stream(row).
                        filter(Objects::nonNull).
                        map(Inline::getButton).
                        collect(InlineKeyboardRow::new, InlineKeyboardRow::add, InlineKeyboardRow::addAll)));

        return new InlineKeyboardMarkup(rows);
    }

    /**
     * Get resizable inline keyboard
     *
     * @param buttonsData - buttons data
     * @param rowSize     - size of each row
     * @return inline keyboard
     */
    public static InlineKeyboardMarkup getResizableKeyboard(List<? extends Button.Inline> buttonsData, int rowSize) {
        List<InlineKeyboardRow> rows = new ArrayList<>();
        Queue<Button.Inline> buttonsQueue = new ArrayDeque<>(buttonsData);

        while (!buttonsQueue.isEmpty())
            rows.add(
                    IntStream.range(0, Math.min(buttonsQueue.size(), rowSize))
                            .mapToObj(i -> buttonsQueue.poll())
                            .filter(Objects::nonNull)
                            .map(Inline::getButton)
                            .collect(InlineKeyboardRow::new, InlineKeyboardRow::add, InlineKeyboardRow::addAll));

        return new InlineKeyboardMarkup(rows);
    }

    /**
     * Get inline keyboard button
     *
     * @param button - button data
     * @see Button.Inline
     * @return inline keyboard button
     */
    private static InlineKeyboardButton getButton(Button.Inline button) {
        InlineKeyboardButton tempInlineButton = new InlineKeyboardButton(button.getText());

        Optional.ofNullable(button.getWebAppInfo()).ifPresent(tempInlineButton::setWebApp);
        Optional.ofNullable(button.getCallback()).ifPresent(callbackBundle -> tempInlineButton.setCallbackData(callbackBundle.toCallback()));

        return tempInlineButton;
    }


}
