package edu.java.bot.command.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import edu.java.bot.service.LinkTrackerService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тесты для {@link ListCommand}.
 */
@ExtendWith(MockitoExtension.class)
class ListCommandTest {

    private static final String COMMAND_FIELD_NAME = "COMMAND_NAME";
    private static final String DESCRIPTION = "description for command";
    private static final String ANSWER = "answer to command";
    private static final String EMPTY_STRING = "";
    private static final String RESOURCES = "link1\nlink2";
    private static final long CHAT_ID = 1;
    private static final long USER_ID = 1;
    private static final BotLocale BOT_LOCALE = BotLocale.DEFAULT;

    @Mock
    private MessageResolver messageResolver;

    @Mock
    private LinkTrackerService linkTrackerService;

    @Mock
    private TelegramBot bot;

    @InjectMocks
    private ListCommand listCommand;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private Chat chat;

    @Mock
    private User user;

    @Test
    void getCommand_returnCorrectCommandName() {
        String result = listCommand.getCommand();

        assertEquals(getPSFString(ListCommand.class, COMMAND_FIELD_NAME), result);
    }

    @ParameterizedTest
    @EnumSource(BotLocale.class)
    void getDescription_callsMessageResolverWithCorrectParameters(BotLocale locale) {
        when(messageResolver.resolve(any(), any())).thenReturn(DESCRIPTION);

        String result = listCommand.getDescription(locale);

        assertEquals(DESCRIPTION, result);
        verify(messageResolver, times(1)).resolve(MessageKey.COMMANDS_LIST_DESCRIPTION, locale);
    }

    @Test
    void handle_noAnyTrackedLinks_sendSpecifiedAnswer() {
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(CHAT_ID);
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(USER_ID);
        when(messageResolver.resolve(any(), any())).thenReturn(ANSWER);
        when(linkTrackerService.getTrackedLinks(anyLong())).thenReturn(EMPTY_STRING);

        try (MockedStatic<BotLocale> botLocaleMock = Mockito.mockStatic(BotLocale.class)) {
            botLocaleMock.when(() -> BotLocale.fromUpdate(any())).thenReturn(BOT_LOCALE);

            listCommand.handle(update);

            botLocaleMock.verify(() -> BotLocale.fromUpdate(update), times(1));

            verify(
                messageResolver,
                times(1)
            ).resolve(
                MessageKey.COMMANDS_LIST_LIST_IS_EMPTY,
                BOT_LOCALE
            );

            verify(bot, times(1)).execute(any());
        }
    }

    @Test
    void handle_trackedLinksAreExists_sendDefaultAnswer() {
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(CHAT_ID);
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(USER_ID);
        when(messageResolver.resolve(any(), any())).thenReturn(ANSWER);
        when(linkTrackerService.getTrackedLinks(anyLong())).thenReturn(RESOURCES);

        try (MockedStatic<BotLocale> botLocaleMock = Mockito.mockStatic(BotLocale.class)) {
            botLocaleMock.when(() -> BotLocale.fromUpdate(any())).thenReturn(BOT_LOCALE);

            listCommand.handle(update);

            botLocaleMock.verify(() -> BotLocale.fromUpdate(update), times(1));

            verify(
                messageResolver,
                times(1)
            ).resolve(
                MessageKey.COMMANDS_LIST_ANSWER,
                BOT_LOCALE
            );

            verify(bot, times(1)).execute(any());
        }
    }

    @Test
    void handle_serviceThrowException_sendSpecifiedAnswer() {
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(CHAT_ID);
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(USER_ID);
        when(messageResolver.resolve(any(), any())).thenReturn(ANSWER);
        when(linkTrackerService.getTrackedLinks(anyLong())).thenThrow(RuntimeException.class);

        try (MockedStatic<BotLocale> botLocaleMock = Mockito.mockStatic(BotLocale.class)) {
            botLocaleMock.when(() -> BotLocale.fromUpdate(any())).thenReturn(BOT_LOCALE);

            listCommand.handle(update);

            botLocaleMock.verify(() -> BotLocale.fromUpdate(update), times(1));

            verify(
                messageResolver,
                times(1)
            ).resolve(
                MessageKey.INTERNAL_ERROR,
                BOT_LOCALE
            );

            verify(bot, times(1)).execute(any());
        }
    }

}
