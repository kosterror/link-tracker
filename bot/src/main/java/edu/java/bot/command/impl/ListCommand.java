package edu.java.bot.command.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import edu.java.bot.service.LinkTrackerService;
import edu.java.bot.util.i18n.BotLocale;
import edu.java.bot.util.i18n.MessageKey;
import edu.java.bot.util.i18n.MessageResolver;
import io.netty.util.internal.StringUtil;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Реализация {@link Command}. Представляет собой команду {@code /list}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ListCommand implements Command {

    private static final String COMMAND_NAME = "/list";

    private final MessageResolver messageResolver;
    private final LinkTrackerService linkTrackerService;
    private final TelegramBot bot;

    @Override
    public String getCommand() {
        return COMMAND_NAME;
    }

    @Override
    public String getDescription(BotLocale botLocale) {
        return messageResolver.resolve(MessageKey.COMMANDS_LIST_DESCRIPTION, botLocale);
    }

    @Override
    public void handle(Update update) {
        long chatId = update.message().chat().id();
        long tgUserId = update.message().from().id();
        BotLocale locale = BotLocale.fromUpdate(update);

        try {
            log.info("Started getting list monitoring resources...");
            String resources = linkTrackerService.getTrackedLinks(tgUserId);
            log.info("Getting list monitoring resources is finished");

            if (Objects.equals(resources, StringUtil.EMPTY_STRING)) {
                sendMessage(chatId, messageResolver.resolve(MessageKey.COMMANDS_LIST_LIST_IS_EMPTY, locale));
                return;
            }

            String answer = messageResolver.resolve(MessageKey.COMMANDS_LIST_ANSWER, locale);
            sendMessage(chatId, answer + "\n" + resources);
        } catch (Exception exception) {
            log.error("Internal error", exception);
            sendMessage(chatId, messageResolver.resolve(MessageKey.INTERNAL_ERROR, locale));
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);
        bot.execute(message);
    }
}
