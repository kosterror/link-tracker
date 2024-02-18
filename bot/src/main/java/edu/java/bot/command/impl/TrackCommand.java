package edu.java.bot.command.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import edu.java.bot.exception.IllegalCommandFormatException;
import edu.java.bot.exception.IllegalLinkFormatException;
import edu.java.bot.exception.LinkAlreadyTrackedException;
import edu.java.bot.service.LinkTrackerService;
import edu.java.bot.util.i18n.BotLocale;
import edu.java.bot.util.i18n.MessageKey;
import edu.java.bot.util.i18n.MessageResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Реализация {@link Command} для команды {@code /track}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TrackCommand implements Command {

    private static final String COMMAND_NAME = "/track";

    private final LinkTrackerService linkTrackerService;
    private final MessageResolver messageResolver;
    private final TelegramBot bot;

    @Override
    public String getCommand() {
        return COMMAND_NAME;
    }

    @Override
    public String getDescription(BotLocale botLocale) {
        return messageResolver.resolve(MessageKey.COMMANDS_TRACK_DESCRIPTION, botLocale);
    }

    @Override
    public MessageEntity.Type getType() {
        return MessageEntity.Type.bot_command;
    }

    @Override
    public void handle(Update update) {
        long chatId = update.message().chat().id();
        BotLocale locale = BotLocale.fromUpdate(update);

        try {
            log.info("Starting to track the link...");
            linkTrackerService.trackLink(update);
            log.info("Starting to track the link is finished");
            sendMessage(chatId, MessageKey.COMMANDS_TRACK_ANSWER, locale);
        } catch (IllegalCommandFormatException exception) {
            log.warn("Format of command is invalid", exception);
            sendMessage(
                chatId,
                MessageKey.COMMANDS_TRACK_ILLEGAL_COMMAND_FORMAT,
                locale
            );
        } catch (IllegalLinkFormatException exception) {
            log.warn("Format of link is invalid", exception);
            sendMessage(
                chatId,
                MessageKey.COMMANDS_TRACK_ILLEGAL_LINK_FORMAT,
                locale
            );
        } catch (LinkAlreadyTrackedException exception) {
            log.warn("Link already tracked", exception);
            sendMessage(
                chatId,
                MessageKey.COMMANDS_TRACK_LINK_ALREADY_TRACKED,
                locale
            );
        } catch (Exception exception) {
            log.error("Internal error", exception);
            sendMessage(chatId, MessageKey.INTERNAL_ERROR, locale);
        }
    }

    private void sendMessage(long chatId, MessageKey key, BotLocale locale) {
        SendMessage message = new SendMessage(chatId, messageResolver.resolve(key, locale));
        bot.execute(message);
    }
}
