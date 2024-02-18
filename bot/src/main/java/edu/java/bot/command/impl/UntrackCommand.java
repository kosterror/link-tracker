package edu.java.bot.command.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import edu.java.bot.exception.IllegalCommandFormatException;
import edu.java.bot.exception.LinkNotFoundException;
import edu.java.bot.service.LinkTrackerService;
import edu.java.bot.util.i18n.BotLocale;
import edu.java.bot.util.i18n.MessageKey;
import edu.java.bot.util.i18n.MessageResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Реализация {@link Command}, представляет собой команду {@code /untrack}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UntrackCommand implements Command {

    private static final String COMMAND_NAME = "/untrack";

    private final MessageResolver messageResolver;
    private final LinkTrackerService linkTrackerService;
    private final TelegramBot telegramBot;

    @Override
    public String getCommand() {
        return COMMAND_NAME;
    }

    @Override
    public String getDescription(BotLocale botLocale) {
        return messageResolver.resolve(MessageKey.COMMANDS_UNTRACK_DESCRIPTION, botLocale);
    }

    @Override
    public void handle(Update update) {
        try {
            log.info("Started stopping tracking link...");
            linkTrackerService.untrackLink(update);
            log.info("Stopping tracking link is finished");
            sendMessage(MessageKey.COMMANDS_UNTRACK_ANSWER, update);
        } catch (LinkNotFoundException exception) {
            log.warn("Link was not found");
            sendMessage(MessageKey.COMMANDS_UNTRACK_LINK_NOT_FOUND, update);
        } catch (IllegalCommandFormatException exception) {
            log.warn("Illegal /untrack command format", exception);
            sendMessage(MessageKey.COMMANDS_UNTRACK_ILLEGAL_COMMAND_FORMAT, update);
        } catch (Exception exception) {
            log.error("Internal error", exception);
            sendMessage(MessageKey.INTERNAL_ERROR, update);
        }
    }

    private void sendMessage(MessageKey key, Update update) {
        SendMessage sendMessage = new SendMessage(
            update.message().chat().id(),
            messageResolver.resolve(key, BotLocale.fromUpdate(update))
        ).parseMode(ParseMode.Markdown);
        telegramBot.execute(sendMessage);
    }

}
