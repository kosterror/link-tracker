package edu.java.bot.util.i18n;

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
}
