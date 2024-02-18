package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.util.i18n.BotLocale;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Интерфейс команды, чтобы реагировать на сообщение/команды пользователя.
 */
public interface Command {

    /**
     * Возвращает текст команды.
     *
     * @return текст команды.
     */
    String getCommand();

    /**
     * Возвращает описание команды.
     *
     * @return описание команды.
     */
    String getDescription(BotLocale botLocale);

    /**
     * Возвращает тип команды.
     *
     * @return тип команды.
     */
    MessageEntity.Type getType();

    /**
     * Обрабатывает переданное событие.
     *
     * @param update событие.
     */
    void handle(Update update);

    /**
     * Определяет: может ли данная команда обработать переданное событие.
     *
     * @param update событие.
     * @return может ли обработать текущая команда переданное событие.
     */
    default boolean isSupports(@NotNull Update update) {
        Message message = update.message();

        if (message == null) {
            return false;
        }

        MessageEntity[] messageEntities = message.entities();

        if (messageEntities == null) {
            return false;
        }

        for (MessageEntity messageEntity : messageEntities) {
            if (Objects.equals(getType(), messageEntity.type()) &&
                message.text() != null &&
                message.text().startsWith(getCommand())
            ) {
                return true;
            }
        }

        return false;
    }

    /**
     * Приводит объект {@link Command} в объект {@link BotCommand}.
     *
     * @return объект {@link BotCommand}.
     */
    default BotCommand toBotCommand(BotLocale botLocale) {
        return new BotCommand(getCommand(), getDescription(botLocale));
    }

}
