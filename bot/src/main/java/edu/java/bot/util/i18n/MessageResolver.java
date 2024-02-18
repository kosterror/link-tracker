package edu.java.bot.util.i18n;

import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

/**
 * Класс, предоставляющий методы для взаимодействия с {@code Resource Bundle}.
 */
@Service
@RequiredArgsConstructor
public class MessageResolver {

    private final ResourceBundleMessageSource resourceSource;

    /**
     * Возвращает сообщение.
     *
     * @param key       ключ сообщения.
     * @param locale    локализация сообщения.
     * @param arguments аргументы, которые можно передать в сообщение.
     * @return сообщение.
     */
    public String resolve(MessageKey key, BotLocale locale, Object... arguments) {
        return resourceSource.getMessage(
            key.getCode(),
            arguments,
            Locale.forLanguageTag(locale.getValue())
        );
    }

}
