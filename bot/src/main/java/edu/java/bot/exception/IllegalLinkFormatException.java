package edu.java.bot.exception;

import edu.java.bot.configuration.UrlValidatorConfiguration;

/**
 * Исключение для случаев, когда формат ссылки является невалидным. В основном для валидации используется
 * бин {@link UrlValidatorConfiguration#urlValidator()}.
 */
public class IllegalLinkFormatException extends RuntimeException {

    public IllegalLinkFormatException(String message) {
        super(message);
    }

}
