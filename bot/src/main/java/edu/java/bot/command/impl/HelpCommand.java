package edu.java.bot.command.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import edu.java.bot.util.i18n.BotLocale;
import edu.java.bot.util.i18n.MessageKey;
import edu.java.bot.util.i18n.MessageResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Реализует {@link Command}, является командой {@code /help}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HelpCommand implements Command {

    private static final String COMMAND_NAME = "/help";

    private final MessageResolver messageResolver;
    private final TelegramBot telegramBot;

    @Override
    public String getCommand() {
        return COMMAND_NAME;
    }

    @Override
    public String getDescription(BotLocale botLocale) {
        return messageResolver.resolve(MessageKey.COMMANDS_HELP_DESCRIPTION, botLocale);
    }

    @Override
    public MessageEntity.Type getType() {
        return MessageEntity.Type.bot_command;
    }

    @Override
    public void handle(Update update) {
        String messageText = messageResolver.resolve(MessageKey.COMMANDS_HELP_ANSWER, BotLocale.fromUpdate(update));
        SendMessage sendMessage = new SendMessage(
            update.message().chat().id(),
            messageText
        ).parseMode(ParseMode.Markdown);

        telegramBot.execute(sendMessage);
    }
}
