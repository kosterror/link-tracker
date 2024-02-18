package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;

/**
 * Интерфейс для взаимодействия со ссылками на ресурсы.
 */
public interface LinkTrackerService {

    /**
     * Начинает отслеживать ссылку из сообщения.
     *
     * @param update событие.
     */
    void trackLink(Update update);

    /**
     * Отменяет отслеживания ссылки из сообщения.
     *
     * @param update событие.
     */
    void untrackLink(Update update);

    /**
     * Возвращает строку, содержащую в себе список отслеживаемых ресурсов. Ресурсы разделены между
     * собой знаками переноса. Если список отслеживаемых ресурсов пуст, то вернется пустая строка.
     *
     * @return отслеживаемы ресурсы.
     */
    String getTrackedLinks(long tgUserId);
}
