package edu.java.bot.exception;

/**
 * Исключение для случаев, когда при добавлении заданная ссылка уже отслеживается.
 */
public class LinkAlreadyTrackedException extends RuntimeException {

    public LinkAlreadyTrackedException(String message) {
        super(message);
    }

}
