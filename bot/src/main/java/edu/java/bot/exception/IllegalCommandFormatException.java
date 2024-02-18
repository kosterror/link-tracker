package edu.java.bot.exception;

/**
 * Исключение для случаев, когда формат команды является некорректным.
 */
public class IllegalCommandFormatException extends RuntimeException {

    public IllegalCommandFormatException(String message) {
        super(message);
    }
}
