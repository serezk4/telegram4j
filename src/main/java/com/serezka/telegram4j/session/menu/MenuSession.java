package com.serezka.telegram4j.session.menu;

import com.serezka.database.authorization.model.User;
import com.serezka.telegram4j.broker.MessageBroker;
import com.serezka.telegram4j.keyboard.Button;
import com.serezka.telegram4j.keyboard.Keyboard;
import com.serezka.telegram4j.keyboard.inline.Callback;
import com.serezka.telegram4j.session.Session;
import com.serezka.telegram4j.session.menu.page.Page;
import com.serezka.telegram4j.util.UpdateUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * @author serezk4
 * @version 1.0
 * @since 1.12
 * <p>
 * Class for menu session
 * e.g. for handling menu
 */

@Log4j2
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class MenuSession implements Session {
    // session id
    static long idCounter = 0;
    @Getter final long id = idCounter++;

    // configuration
    final MenuSessionConfiguration configuration;
    final User user;
    final MessageBroker broker;

    // buttons that are always at the menu keyboard
    final List<Button.Inline> top = List.of();
    final List<Button.Inline> bottom = List.of(
            new Button.Inline("\uD83D\uDD19", Callback.fromLink("back")),  // back
            new Button.Inline("\uD83C\uDFE0", Callback.fromLink("root")),  // home
            new Button.Inline("🤏", Callback.fromLink("close"))            // close
    );

    final InlineKeyboardRow topRow = new InlineKeyboardRow(top.stream().map(Button.Inline::toInlineKeyboardButton).toList());
    final InlineKeyboardRow bottomRow = new InlineKeyboardRow(bottom.stream().map(Button.Inline::toInlineKeyboardButton).toList());

    // current page
    Page.Generator currentPage;

    // messageid storage
    final Deque<Integer> messageIds = new LinkedList<>();

    public boolean containsMessage(int messageId) {
        return messageIds.contains(messageId);
    }

    @Override
    public void init(Update update) {
        log.info("Initiating menu session for user {} | menu id: {}", user, id);
        broadcast(configuration.getRoot().apply(this, user, update));
    }

    @Override
    public void next(Update update) {
        log.info("Handling menu session for user {} | menu id: {}", user, id);

        String text = UpdateUtil.getText(update);
        Callback callback = Callback.fromCallback(text);

        switch (callback.link()) {
            case "root" -> {
                init(update);
                return;
            }
            case "close" -> {
                close();
                return;
            }
            case "back" -> {
                back(update);
                return;
            }
        }

        currentPage = pick(callback.link());
        broadcast(currentPage.apply(this, user, update));
    }

    private void back(Update update) {

    }

    private Page.Generator pick(String link) {
        if (link == null || link.isBlank()) return currentPage;
        return configuration.getPages().getOrDefault(link, null);
    }

    private void broadcast(Page page) {
        if (messageIds.isEmpty()) send(page);
        else editText(page);
    }

    private void sendMedia(Page page) {
        if (page.getKeyboard() != null && page.getKeyboard().toReplyKeyboard() instanceof InlineKeyboardMarkup inlineKeyboard) {
            inlineKeyboard.getKeyboard().addFirst(topRow);
            inlineKeyboard.getKeyboard().add(bottomRow);

            messageIds.add(broker.execute(SendPhoto.builder()
                    .chatId(user.getChatId())
                    .caption(page.getText())
                    .replyMarkup(inlineKeyboard)
                    .build()
            ).getMessageId());

            return;
        }

        messageIds.add(broker.execute(SendPhoto.builder()
                .chatId(user.getChatId())
                .caption(page.getText())
                .replyMarkup(page.getKeyboard().toReplyKeyboard())
                .build()
        ).getMessageId());
    }

    private void send(Page page) {
        if (page.getInputMedia() != null) {
            sendMedia(page);
            return;
        }

        if (page.getKeyboard() != null && page.getKeyboard().toReplyKeyboard() instanceof InlineKeyboardMarkup inlineKeyboard) {
            inlineKeyboard.getKeyboard().addFirst(topRow);
            inlineKeyboard.getKeyboard().add(bottomRow);

            messageIds.add(broker.sendMessage(user.getChatId(), page.getText(), inlineKeyboard).getMessageId());

            return;
        }



        messageIds.add(broker.sendMessage(user.getChatId(), page.getText(), Optional.ofNullable(page.getKeyboard()).map(Keyboard::toReplyKeyboard).orElse(null)).getMessageId());
    }

    private void editMedia(Page page) {

        if (page.getText() != null) {
            broker.execute(EditMessageCaption.builder()
                    .chatId(user.getChatId()).messageId(messageIds.peekLast())
                    .caption(page.getText())
                    .build());
        }

        if (page.getInputMedia() != null) {
            broker.execute(EditMessageMedia.builder()
                    .chatId(user.getChatId()).messageId(messageIds.peekLast())
                    .media(page.getInputMedia())
                    .build()
            );
        }

        if (page.getKeyboard() != null && page.getKeyboard().toReplyKeyboard() instanceof InlineKeyboardMarkup inlineKeyboard) {
            inlineKeyboard.getKeyboard().addFirst(topRow);
            inlineKeyboard.getKeyboard().add(bottomRow);

            broker.execute(EditMessageReplyMarkup.builder()
                    .chatId(user.getChatId()).messageId(messageIds.peekLast())
                    .replyMarkup(inlineKeyboard)
                    .build());
            return;
        }
    }

    private void editText(Page page) {
        if (page.getKeyboard() != null && page.getKeyboard().toReplyKeyboard() instanceof InlineKeyboardMarkup inlineKeyboard) {
            inlineKeyboard.getKeyboard().addFirst(topRow);
            inlineKeyboard.getKeyboard().add(bottomRow);

            broker.execute(EditMessageReplyMarkup.builder()
                    .chatId(user.getChatId()).messageId(messageIds.peekLast())
                    .replyMarkup(inlineKeyboard)
                    .build());
            return;
        }

        if (page.getText() != null) {
            broker.execute(EditMessageText.builder()
                    .chatId(user.getChatId()).messageId(messageIds.peekLast())
                    .text(page.getText())
                    .build());
        }

    }


    @Override
    public void close() {

    }
}
