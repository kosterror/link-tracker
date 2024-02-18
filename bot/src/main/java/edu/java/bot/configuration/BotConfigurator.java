package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.command.Command;
import edu.java.bot.util.i18n.BotLocale;
import edu.java.bot.util.i18n.MessageKey;
import edu.java.bot.util.i18n.MessageResolver;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурирует {@link TelegramBot}, отправляя запросы в {@code Telegram API}.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class BotConfigurator implements AutoCloseable, UpdatesListener {

    private final TelegramBot bot;
    private final List<Command> commands;
    private final MessageResolver messageResolver;

    /**
     * Устанавливает {@link UpdatesListener} и {@link BotCommand} для бота. После этого происходит прослушивание
     * событий, и в меню бота отображаются установленные команды.
     */
    @PostConstruct
    void start() {
        log.info("Started setting update listener...");
        bot.setUpdatesListener(this);
        log.info("Setting update listener is finished");

        setCommands(BotLocale.RU);
        setCommands(BotLocale.DEFAULT);
    }

    /**
     * Выключает бота.
     */
    @Override
    public void close() {
        log.info("Started bot shutdown...");
        bot.shutdown();
        log.info("Bot shutdown is finished");
    }

    /**
     * Точка входа всех событий с ботом в приложении. Если для события есть подходящая реализация {@link Command},
     * то обработка события будет передана этой реализации. Если ничего не нашлось, то событие будет проигнорировано
     * и отмечено, как обработанное.
     *
     * @param updates события.
     * @return идентификатор последнего обработанного события.
     */
    @Override public int process(List<Update> updates) {
        for (Update update : updates) {
            Optional<Command> commandOptional = commands.stream()
                .filter(command -> command.isSupports(update))
                .findFirst();

            if (commandOptional.isPresent()) {
                commandOptional.get().handle(update);
            } else {
                Message message = update.message();
                if (message != null
                    && message.text() != null
                    && message.chat() != null
                    && message.chat().id() != null) {
                    SendMessage sendMessage = new SendMessage(
                        message.chat().id(),
                        messageResolver.resolve(MessageKey.INVALID_COMMAND, BotLocale.fromUpdate(update))
                    );
                    bot.execute(sendMessage);
                }
            }
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void setCommands(BotLocale botLocale) {
        log.info("Started setting commands with {} localization...", botLocale.getValue());
        SetMyCommands setMyCommands = new SetMyCommands(convertCommands(commands, botLocale));
        bot.execute(setMyCommands);
        log.info("Setting commands with {} localization is finished", botLocale.getValue());
    }

    private BotCommand[] convertCommands(List<Command> commands, BotLocale locale) {
        List<BotCommand> botCommandList = commands.stream().map(command -> command.toBotCommand(locale)).toList();
        return botCommandList.toArray(BotCommand[]::new);
    }

}
