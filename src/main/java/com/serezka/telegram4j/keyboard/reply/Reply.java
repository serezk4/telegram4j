package com.serezka.telegram4j.keyboard.reply;

import com.serezka.telegram4j.keyboard.Button;
import com.serezka.telegram4j.keyboard.Keyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;

import java.util.*;
import java.util.stream.IntStream;

/**
 * @author serezk4
 * @version 1.0
 * <p>
 * Util class for reply keyboard
 * @since 1.11
 */

public class Reply {
    /**
     * Get custom reply keyboard
     *
     * @param buttonsText - buttons text
     *                    <p>Example: {@code List.of(List.of("button1", "button2"), List.of("button3"))}
     * @return custom reply keyboard
     */
    public static ReplyKeyboardMarkup getCustomKeyboard(List<List<String>> buttonsText) {
        return getCustomKeyboard(buttonsText, false);
    }

    // todo
//    /**
//     * Get custom reply keyboard
//     *
//     * @param buttonsText - buttons text
//     *                    <p>Example: {@code List.of(List.of("button1", "button2"), List.of("button3"))}
//     * @param addButtons  - add default buttons
//     *                    <p> {@link Keyboard.Actions#BACK} and {@link Keyboard.Actions#CLOSE} buttons will be added if
//     *                    <p> {@code true}
//     * @return custom reply keyboard with or without default buttons {@link Keyboard.Actions#BACK} and {@link Keyboard.Actions#CLOSE}
//     */
    public static ReplyKeyboardMarkup getCustomKeyboard(List<List<String>> buttonsText, boolean addButtons) {
//        if (addButtons) buttonsText.add(List.of(Keyboard.Actions.BACK.getName(), Keyboard.Actions.CLOSE.getName()));
        ReplyKeyboardMarkup replyKeyboard = new ReplyKeyboardMarkup(buttonsText.stream()
                .map(row -> new KeyboardRow(
                        row.stream()
                                .filter(Objects::nonNull)
                                .map(Reply::getButton).toList()))
                .toList());

        replyKeyboard.setResizeKeyboard(true);

        return replyKeyboard;
    }

    /**
     * Get custom reply keyboard
     *
     * @param buttonsText - buttons text
     *                    <p>Example: {@code new String[][]{{"button1", "button2"}, {"button3"}}}
     * @return custom reply keyboard
     */
    public static ReplyKeyboardMarkup getCustomKeyboard(String[][] buttonsText) {
        return getCustomKeyboard(Arrays.stream(buttonsText).map(Arrays::asList).toList());
    }

    /**
     * Get resizable reply keyboard
     *
     * @param buttons - buttons
     *                <p>Example: {@code List.of(new Button.Reply("button1"), new Button.Reply("button2"))}
     * @param rowSize - size of each row
     *                <p>Example: {@code 2} - {@code button1 button2 \n button3 button4}
     *                <p>Example: {@code 3} - {@code button1 button2 button3 \n button4 button5 button6}
     * @return resizable reply keyboard
     */
    public static ReplyKeyboardMarkup getResizableKeyboard(List<Button.Reply> buttons, int rowSize) {
        List<KeyboardRow> mainRow = new ArrayList<>();
        Queue<Button.Reply> buttonsQueue = new ArrayDeque<>(buttons);

        while (!buttonsQueue.isEmpty()) {
            mainRow.add(new KeyboardRow(
                    IntStream.range(0, Math.min(rowSize, buttonsQueue.size()))
                            .mapToObj(i -> buttonsQueue.poll())
                            .filter(Objects::nonNull)
                            .map(Reply::getButton)
                            .toList()
            ));
        }

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(mainRow);
        replyKeyboardMarkup.setResizeKeyboard(true);

        return replyKeyboardMarkup;
    }

    /**
     * Get keyboard button
     *
     * @param button - button data
     *               <p>Example: {@code new Button.Reply("button1")}
     * @return keyboard button
     */
    private static KeyboardButton getButton(Button.Reply button) {
        return getButton(button.getText(), button.getWebAppInfo());
    }

    /**
     * Get keyboard button
     *
     * @param text       - button text
     * @param webAppInfo - web app info
     *                   <p>Example: {@code "button1", webAppInfo}
     * @return keyboard button
     */
    private static KeyboardButton getButton(String text, WebAppInfo webAppInfo) {
        KeyboardButton tempButton = new KeyboardButton(text);
        Optional.ofNullable(webAppInfo).ifPresent(tempButton::setWebApp);

        return tempButton;
    }

    /**
     * Get keyboard button
     *
     * @param text - button text
     *             <p>Example: {@code "button1"}
     * @return keyboard button
     */
    private static KeyboardButton getButton(String text) {
        return getButton(text, null);
    }

}
