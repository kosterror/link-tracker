package edu.java.bot.command.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import edu.java.bot.util.i18n.BotLocale;
import edu.java.bot.util.i18n.MessageKey;
import edu.java.bot.util.i18n.MessageResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Реализация {@link Command} для команды {@code /start}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StartCommand implements Command {

    private static final String COMMAND_NAME = "/start";

    private final TelegramBot telegramBot;
    private final MessageResolver messageResolver;

    @Override
    public String getCommand() {
        return COMMAND_NAME;
    }

    @Override
    public String getDescription(BotLocale botLocale) {
        return messageResolver.resolve(MessageKey.COMMANDS_START_DESCRIPTION, botLocale);
    }

    @Override
    public MessageEntity.Type getType() {
        return MessageEntity.Type.bot_command;
    }

    @Override
    public void handle(Update update) {
        log.info("Simulation the launch of the bot functionality for this user");
        SendMessage sendMessage = new SendMessage(
            update.message().chat().id(),
            messageResolver.resolve(MessageKey.COMMANDS_START_ANSWER, BotLocale.fromUpdate(update))
        );

        telegramBot.execute(sendMessage);
    }
}
