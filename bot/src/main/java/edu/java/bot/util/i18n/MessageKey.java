package edu.java.bot.util.i18n;

import lombok.Getter;

/**
 * Перечисление, оборачивающая в себя ключи от {@code Resource Bundle}.
 */
@Getter
public enum MessageKey {
    COMMANDS_START_DESCRIPTION("commands.start.description"),
    COMMANDS_START_ANSWER("commands.start.answer");

    /**
     * Ключ от {@code Resource Bundle}.
     */
    private final String code;

    MessageKey(String code) {
        this.code = code;
    }

}
