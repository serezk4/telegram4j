package com.serezka.telegram4j.session.step;

import com.serezka.database.authorization.model.User;
import com.serezka.telegram4j.broker.MessageBroker;
import com.serezka.telegram4j.keyboard.Button;
import com.serezka.telegram4j.keyboard.Keyboard;
import com.serezka.telegram4j.session.Session;
import com.serezka.telegram4j.util.UpdateUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.*;
import java.util.stream.IntStream;

/**
 * @author serezk4
 * @version 1.1
 * @since 1.12
 * <p>
 * Class for step session
 * e.g. for handling step-by-step actions in chat
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class StepSession implements Session {
    final StepSessionConfiguration configuration;
    final User user;
    final MessageBroker broker;

    final Deque<Step.Generator> future;
    final Deque<Step.Generator> past = new LinkedList<>();

    final Deque<Message> botMessages = new LinkedList<>();
    final Set<Integer> userMessagesIds = new HashSet<>();

    final KeyboardRow replyBottomRow = new KeyboardRow("[ x ]");
    final InlineKeyboardRow inlineBottomRow = new InlineKeyboardRow(Button.Inline.fromLink("[ x ]","exit").toInlineKeyboardButton());

    public StepSession(StepSessionConfiguration configuration, User user, MessageBroker broker) {
        this.configuration = configuration;
        this.user = user;
        this.broker = broker;

        this.future = configuration.getSteps();
    }

    @Override
    public void next(Update update) {
        userMessagesIds.add(UpdateUtil.getMessageId(update));

        if (future.isEmpty() || UpdateUtil.getText(update).contains("[x]")) {
            close();
            return;
        }

        Step.Generator generator = pollStepGenerator();
        Step step = generator.apply(this, user, update);
        broadcast(step);

        // todo fix shitcode
        if (future.isEmpty()) close();
    }

    // send methods
    private Step.Generator pollStepGenerator() {
        return future.pollFirst();
    }

    private void broadcast(Step step) {
        if (configuration.isCanEditMessages() && !botMessages.isEmpty() && botMessages.peekLast() != null && botMessages.peekLast().getMessageId() != null) editMessage(step);
        else sendMessage(step);

        if (!configuration.isSaveUsersMessages()) deleteUsersMessages();
        if (!configuration.isSaveBotsMessages()) deleteBotsMessages();
    }

    private void sendMessage(Step step) {
        Optional.ofNullable(broker.sendMessage(user.getChatId(), step.getText(), buildKeyboard(step)))
                .ifPresent((botMessages::add));
    }

    private void editMessage(Step step) {
        Optional.ofNullable(step.getText())
                .ifPresent(text -> broker.editText(user.getChatId(), botMessages.peekLast().getMessageId(), text));

        Optional.ofNullable(step.getKeyboard())
                .map(keyboard -> buildKeyboard(step))
                .filter(InlineKeyboardMarkup.class::isInstance)
                .map(InlineKeyboardMarkup.class::cast)
                .ifPresent(keyboard -> broker.editKeyboard(user.getChatId(), botMessages.peekLast().getMessageId(), keyboard));

//        Optional.ofNullable(step.getInputMedia()) todo
//                .ifPresent(media -> broker.editMedia(user.getChatId(), botMessages.peekLast().getMessageId(), media));
    }

    private ReplyKeyboard buildKeyboard(Step page) {
        return Optional.ofNullable(page.getKeyboard())
                .map(Keyboard::toReplyKeyboard)
                .map(keyboard -> {
                    if (keyboard instanceof ReplyKeyboardMarkup replyKeyboard) {
                        replyKeyboard.getKeyboard().add(replyBottomRow);
                        return replyKeyboard;
                    }

                    if (keyboard instanceof InlineKeyboardMarkup inlineKeyboard) {
                        inlineKeyboard.getKeyboard().add(inlineBottomRow);
                        return inlineKeyboard;
                    }

                    return keyboard;
                }).orElse(new InlineKeyboardMarkup(Collections.singletonList(inlineBottomRow)));
    }

    // delete methods
    private void deleteUsersMessages() {
        userMessagesIds.forEach(messageId -> broker.deleteMessage(user.getChatId(), messageId));
        userMessagesIds.clear();
    }

    private void deleteBotsMessages() {
        while (botMessages.size() > 1) {
            broker.deleteMessage(user.getId(), botMessages.pollFirst().getMessageId());
        }
    }

    // jump methods

    public void forward() {
        if (past.isEmpty()) return;
        future.addFirst(past.pollLast());
    }

    public void forward(int n) {
        IntStream.range(0, n).forEach(this::forward);
    }

    public void forwardTo(int n) {
        IntStream.range(0, past.size() - n).forEach(i -> future.addFirst(past.pollLast()));
    }

    public void rollbackAll() {
        future.addAll(past);
        past.clear();
    }

    public void rollbackTo(int n) {
        IntStream.range(0, future.size() - n).forEach(i -> past.addFirst(future.pollFirst()));
    }

    public void rollback() {
        if (past.isEmpty()) return;
        future.addFirst(past.pollLast());
    }

    public void rollback(int n) {
        IntStream.range(0, n).forEach(this::rollback);
    }

    @Override
    public void close() {
        deleteBotsMessages();
        StepSessionManager.getInstance().remove(this);
    }
}
