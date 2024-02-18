package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
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

    /**
     * Возвращает локализацию, которую следует использовать для ответа на сообщение.
     *
     * @param update событие.
     * @return локализация.
     */
    default BotLocale getLocale(Update update) {
        Message message = update.message();

        if (message == null) {
            return BotLocale.DEFAULT;
        }

        User user = message.from();

        if (user == null) {
            return BotLocale.DEFAULT;
        }

        return user.languageCode() == null ? null : BotLocale.fromLanguageCode(user.languageCode());
    }

}
