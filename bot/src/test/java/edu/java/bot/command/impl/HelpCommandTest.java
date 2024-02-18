package edu.java.bot.command.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.util.i18n.BotLocale;
import edu.java.bot.util.i18n.MessageKey;
import edu.java.bot.util.i18n.MessageResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static edu.java.bot.utils.ReflectionUtils.getPSFString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HelpCommandTest {

    private static final String COMMAND_FIELD_NAME = "COMMAND_NAME";
    private static final String DESCRIPTION = "description for command";
    private static final String ANSWER = "answer to command";
    private static final long CHAT_ID = 1;
    private static final BotLocale BOT_LOCALE = BotLocale.RU;

    @Mock
    private MessageResolver messageResolver;

    @Mock
    private TelegramBot telegramBot;

    @InjectMocks
    private HelpCommand helpCommand;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private Chat chat;

    @Test
    void getCommand_returnsCorrectCommandName() {
        String result = helpCommand.getCommand();

        assertEquals(getPSFString(HelpCommand.class, COMMAND_FIELD_NAME), result);
    }

    @ParameterizedTest
    @EnumSource(BotLocale.class)
    void getDescription_callsMessageResolverWithCorrectParameters(BotLocale locale) {
        when(messageResolver.resolve(any(), any())).thenReturn(DESCRIPTION);
        String result = helpCommand.getDescription(locale);

        assertEquals(DESCRIPTION, result);
        verify(messageResolver, times(1)).resolve(MessageKey.COMMANDS_HELP_DESCRIPTION, locale);
    }

    @Test
    void handle_callsMessageResolverAndTelegramBotWithCorrectParameters() {
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(CHAT_ID);
        when(messageResolver.resolve(any(), any())).thenReturn(ANSWER);

        try (MockedStatic<BotLocale> botLocaleMock = Mockito.mockStatic(BotLocale.class)) {
            botLocaleMock.when(() -> BotLocale.fromUpdate(any())).thenReturn(BOT_LOCALE);

            helpCommand.handle(update);

            botLocaleMock.verify(() -> BotLocale.fromUpdate(update), times(1));

            verify(
                messageResolver,
                times(1)
            ).resolve(
                MessageKey.COMMANDS_HELP_ANSWER,
                BOT_LOCALE
            );

            verify(telegramBot, times(1)).execute(any());

        }
    }

}
