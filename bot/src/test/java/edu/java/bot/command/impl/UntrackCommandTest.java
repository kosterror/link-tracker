package edu.java.bot.command.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.exception.IllegalCommandFormatException;
import edu.java.bot.exception.LinkNotFoundException;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тесты для {@link UntrackCommand}.
 */
@ExtendWith(MockitoExtension.class)
class UntrackCommandTest {

    private static final String COMMAND_FIELD_NAME = "COMMAND_NAME";
    private static final String DESCRIPTION = "command description";
    private static final String ANSWER = "answer to command";
    private static final long CHAT_ID = 1;
    private static final BotLocale BOT_LOCALE = BotLocale.DEFAULT;

    @InjectMocks
    private UntrackCommand command;

    @Mock
    private MessageResolver messageResolver;

    @Mock
    private LinkTrackerService service;

    @Mock
    private TelegramBot bot;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private Chat chat;

    @Test
    void getCommand_returnCorrectCommandName() {
        String result = command.getCommand();

        assertEquals(getPSFString(UntrackCommand.class, COMMAND_FIELD_NAME), result);
    }

    @ParameterizedTest
    @EnumSource(BotLocale.class)
    void getDescription_callsMessageResolverWithCorrectParameters(BotLocale locale) {
        when(messageResolver.resolve(any(), any())).thenReturn(DESCRIPTION);

        String result = command.getDescription(locale);

        assertEquals(DESCRIPTION, result);
        verify(messageResolver, times(1)).resolve(MessageKey.COMMANDS_UNTRACK_DESCRIPTION, locale);
    }

    @Test
    void handle_correctUpdate_returnDefaultAnswer() {
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(CHAT_ID);
        when(messageResolver.resolve(any(), any())).thenReturn(ANSWER);

        try (MockedStatic<BotLocale> botLocaleMock = Mockito.mockStatic(BotLocale.class)) {
            botLocaleMock.when(() -> BotLocale.fromUpdate(any())).thenReturn(BOT_LOCALE);

            command.handle(update);

            verify(service, times(1)).untrackLink(update);
            verify(
                messageResolver,
                times(1)
            ).resolve(
                MessageKey.COMMANDS_UNTRACK_ANSWER,
                BOT_LOCALE
            );
            verify(bot, times(1)).execute(any());
        }
    }

    @Test
    void handle_invalidCommand_returnSpecifiedAnswer() {
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(CHAT_ID);
        when(messageResolver.resolve(any(), any())).thenReturn(ANSWER);
        doThrow(IllegalCommandFormatException.class).when(service).untrackLink(any());

        try (MockedStatic<BotLocale> botLocaleMock = Mockito.mockStatic(BotLocale.class)) {
            botLocaleMock.when(() -> BotLocale.fromUpdate(any())).thenReturn(BOT_LOCALE);

            command.handle(update);

            verify(service, times(1)).untrackLink(update);
            verify(
                messageResolver,
                times(1)
            ).resolve(MessageKey.COMMANDS_UNTRACK_ILLEGAL_COMMAND_FORMAT, BOT_LOCALE);
            verify(bot, times(1)).execute(any());
        }
    }

    @Test
    void handle_linkNotFound_returnSpecifiedAnswer() {
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(CHAT_ID);
        when(messageResolver.resolve(any(), any())).thenReturn(ANSWER);
        doThrow(LinkNotFoundException.class).when(service).untrackLink(any());

        try (MockedStatic<BotLocale> botLocaleMock = Mockito.mockStatic(BotLocale.class)) {
            botLocaleMock.when(() -> BotLocale.fromUpdate(any())).thenReturn(BOT_LOCALE);

            command.handle(update);

            verify(service, times(1)).untrackLink(update);
            verify(
                messageResolver,
                times(1)
            ).resolve(MessageKey.COMMANDS_UNTRACK_LINK_NOT_FOUND, BOT_LOCALE);
            verify(bot, times(1)).execute(any());
        }
    }

    @Test
    void handle_throwException_returnSpecifiedAnswer() {
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(CHAT_ID);
        when(messageResolver.resolve(any(), any())).thenReturn(ANSWER);
        doThrow(RuntimeException.class).when(service).untrackLink(any());

        try (MockedStatic<BotLocale> botLocaleMock = Mockito.mockStatic(BotLocale.class)) {
            botLocaleMock.when(() -> BotLocale.fromUpdate(any())).thenReturn(BOT_LOCALE);

            command.handle(update);

            verify(service, times(1)).untrackLink(update);
            verify(
                messageResolver,
                times(1)
            ).resolve(MessageKey.INTERNAL_ERROR, BOT_LOCALE);
            verify(bot, times(1)).execute(any());
        }
    }

}
