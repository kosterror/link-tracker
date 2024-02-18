package edu.java.bot.util.i18n;

import com.pengrad.telegrambot.model.Update;
import lombok.Getter;

/**
 * Локализация бота.
 */
@Getter
public enum BotLocale {
    DEFAULT("en"),
    RU("ru");

    private final String value;

    BotLocale(String value) {
        this.value = value;
    }

    /**
     * Получить значение {@link BotLocale} на основе строки.
     *
     * @param languageCode локализация, представленная двумя буквами.
     * @return соответствующие значение {@link BotLocale}.
     */
    public static BotLocale fromLanguageCode(String languageCode) {
        for (BotLocale botLocale : values()) {
            if (botLocale.getValue().equalsIgnoreCase(languageCode)) {
                return botLocale;
            }
        }

        return BotLocale.DEFAULT;
    }

    /**
     * Получить значение {@link BotLocale} на основе объекта {@link Update}.
     *
     * @param update событие.
     * @return локализация.
     */
    public static BotLocale fromUpdate(Update update) {
        if (update != null &&
            update.message() != null &&
            update.message().from() != null &&
            update.message().from().languageCode() != null) {
            return fromLanguageCode(update.message().from().languageCode());
        }

        return BotLocale.DEFAULT;
    }
}
