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

import static java.util.function.Predicate.not;

/**
 * @author serezk4
 * @version 1.1 [beta]
 * @since 1.12
 * <p>
 * Class for menu session
 * e.g. for handling menu
 */

@Log4j2
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class MenuSession implements Session {
    static long idCounter = 0;
    @Getter
    final long id = idCounter++;

    final MenuSessionConfiguration configuration;
    final User user;
    final MessageBroker broker;

    final List<Button.Inline> top = List.of();
    final List<Button.Inline> bottom = List.of(
            new Button.Inline("\uD83D\uDD19", Callback.fromLink("back")),  // back
            new Button.Inline("\uD83C\uDFE0", Callback.fromLink("root")),  // home
            new Button.Inline("🤏", Callback.fromLink("close"))            // close
    );

    final InlineKeyboardRow topRow = new InlineKeyboardRow(top.stream().map(Button.Inline::toInlineKeyboardButton).toList());
    final InlineKeyboardRow bottomRow = new InlineKeyboardRow(bottom.stream().map(Button.Inline::toInlineKeyboardButton).toList());

    Page.Generator currentPage;
    final Map<Page.Generator, Page.Generator> trace = new HashMap<>();
    final Deque<Integer> messageIds = new LinkedList<>();

    public boolean containsMessage(int messageId) {
        return messageIds.contains(messageId);
    }

    @Override
    public void init(Update update) {
        log.info("Initiating menu session for user {} | menu id: {}", user, id);
        update((currentPage = configuration.getRoot()).apply(this, user, update));
    }

    @Override
    public void next(Update update) {
        log.info("Handling menu session for user {} | menu id: {}", user, id);

        Callback callback = Callback.fromCallback(UpdateUtil.getText(update));

        switch (callback.link()) {
            case "root" -> init(update);
            case "close" -> close();
            case "back" -> navigateBack(update);
            default -> {
                Page.Generator before = currentPage;

                if (!callback.link().isBlank()) currentPage = resolvePage(callback.link());
                if (currentPage == null) log.warn("Page {} not found", callback.link());
                currentPage = currentPage == null ? before : currentPage;
                if (before != currentPage) trace.put(currentPage, before);
                update(currentPage.apply(this, user, update));
            }
        }
    }

    private void navigateBack(Update update) {
        Optional.ofNullable(trace.remove(currentPage))
                .ifPresent(previousPage -> update(previousPage.apply(this, user, update)));
    }

    private Page.Generator resolvePage(String link) {
        return Optional.ofNullable(link)
                .filter(not(String::isBlank))
                .map(configuration.getPages()::get)
                .orElse(currentPage);
    }

    private void update(Page page) {
        if (messageIds.isEmpty()) sendMessage(page);
        else editMessage(page);
    }

    private void sendMessage(Page page) {
        Optional.ofNullable(page.getInputMedia()).ifPresentOrElse(media -> sendMedia(page), () -> sendText(page));
    }

    private void sendMedia(Page page) {
        Optional.ofNullable(broker.sendPhoto(user.getChatId(), page.getText(), buildKeyboard(page), page.getInputMedia()))
                .ifPresent(message -> messageIds.add(message.getMessageId()));
    }

    private void sendText(Page page) {
        Optional.ofNullable(broker.sendMessage(user.getChatId(), page.getText(), buildKeyboard(page)))
                .ifPresent(message -> messageIds.add(message.getMessageId()));
    }

    private void editMessage(Page page) {
        if (page.getInputMedia() != null) editMedia(page);
        else editText(page);
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
        return Optional.ofNullable(page.getKeyboard())
                .map(Keyboard::toReplyKeyboard)
                .filter(InlineKeyboardMarkup.class::isInstance)
                .map(InlineKeyboardMarkup.class::cast)
                .map(inlineKeyboard -> {
                    inlineKeyboard.getKeyboard().addFirst(topRow);
                    inlineKeyboard.getKeyboard().add(bottomRow);
                    return inlineKeyboard;
                })
                .orElse(null); // todo
    }

    private void deleteMessage(int messageId) {
        broker.deleteMessage(user.getChatId(), messageId);
    }

    @Override
    public void close() {
        messageIds.forEach(this::deleteMessage);
        messageIds.clear();
    }
}
