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
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.*;

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
    @Getter
    final long id = idCounter++;

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

    // tracking // todo maybe remove
    Map<Page.Generator, Page.Generator> trace = new HashMap<>();

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

        Callback callback = Callback.fromCallback(UpdateUtil.getText(update));

        switch (callback.link()) {
            case "root" -> init(update);
            case "close" -> close();
            case "back" -> back(update);
            default -> {
                Page.Generator before = currentPage;
                if (!callback.link().isBlank()) currentPage = pick(callback.link());
                if (before != currentPage) trace.put(currentPage, before);
                broadcast(currentPage.apply(this, user, update));
            }
        }
    }

    private void back(Update update) {
        if (trace.isEmpty() || !trace.containsKey(currentPage)) return;
        broadcast(trace.remove(currentPage).apply(this, user, update));
    }

    private Page.Generator pick(String link) {
        if (link == null || link.isBlank()) return currentPage;
        return configuration.getPages().getOrDefault(link, null);
    }

    private void broadcast(Page page) {
        if (messageIds.isEmpty() && page.getInputMedia() != null) {
            sendMedia(page);
            return;
        }

        if (messageIds.isEmpty()) {
            send(page);
            return;
        }

        if (page.getInputMedia() != null) {
            editMedia(page);
            return;
        }

        editText(page);
    }

    private void sendMedia(Page page) {
        Optional.ofNullable(broker.sendPhoto(user.getChatId(), page.getText(), buildKeyboard(page), page.getInputMedia()))
                .ifPresent(message -> messageIds.add(message.getMessageId()));
    }

    private void send(Page page) {
        if (page.getInputMedia() != null) {
            sendMedia(page);
            return;
        }

        messageIds.add(broker.sendMessage(user.getChatId(), page.getText(), buildKeyboard(page)).getMessageId());
    }

    private void editMedia(Page page) {
        Optional.ofNullable(page.getText()).ifPresent(text -> broker.editCaption(user.getChatId(), messageIds.peekLast(), text));
        Optional.ofNullable(page.getInputMedia()).ifPresent(media -> broker.editMedia(user.getChatId(), messageIds.peekLast(), media));
        editKeyboard(page);
    }

    private void editText(Page page) {
        Optional.ofNullable(page.getText()).ifPresent(text -> broker.editText(user.getChatId(), messageIds.peekLast(), text));
        editKeyboard(page);
    }

    private void editKeyboard(Page page) {
        Optional.ofNullable(page.getKeyboard())
                .map(Keyboard::toReplyKeyboard)
                .filter(keyboard -> keyboard instanceof InlineKeyboardMarkup)
                .ifPresent(keyboard -> broker.editKeyboard(user.getChatId(), messageIds.peekLast(), (InlineKeyboardMarkup) buildKeyboard(page)));
    }

    private ReplyKeyboard buildKeyboard(Page page) {
        if (page.getKeyboard() == null) return null;

        ReplyKeyboard raw = page.getKeyboard().toReplyKeyboard();

        if (raw instanceof InlineKeyboardMarkup inlineKeyboard) {
            inlineKeyboard.getKeyboard().addFirst(topRow);
            inlineKeyboard.getKeyboard().add(bottomRow);
            return inlineKeyboard;
        }

        return raw;
    }

    private void deleteMessage(int messageId) {
        broker.deleteMessage(user.getChatId(), messageId);
    }

    @Override
    public void close() {
        messageIds.forEach(messageId -> broker.deleteMessage(user.getChatId(), messageId));
        messageIds.clear();
    }
}
