package edu.java.bot.exception;

/**
 * Исключение для случаев, когда не удалось найти заданную ссылку.
 */
public class LinkNotFoundException extends RuntimeException {

    public LinkNotFoundException(String message) {
        super(message);
    }

}
