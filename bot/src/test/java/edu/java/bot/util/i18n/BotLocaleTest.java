package edu.java.bot.util.i18n;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Тесты для {@link BotLocale}.
 */
@ExtendWith(MockitoExtension.class)
class BotLocaleTest {

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private User user;

    @ParameterizedTest
    @MethodSource(value = "provideRuLanguageCodes")
    void fromLanguageCode_ruLanguageCode_returnRuBotLocale(String languageCode) {
        BotLocale result = BotLocale.fromLanguageCode(languageCode);

        assertEquals(BotLocale.RU, result);
    }

    @ParameterizedTest
    @MethodSource(value = "provideUnspecifiedLanguageCodes")
    void fromLanguageCode_unspecifiedLanguageCode_returnDefaultBotLocale(String languageCode) {
        BotLocale result = BotLocale.fromLanguageCode(languageCode);

        assertEquals(BotLocale.DEFAULT, result);
    }

    @Test
    void fromUpdate_updateIsNull_returnDefaultBotLocale() {
        BotLocale result = BotLocale.fromUpdate(null);

        assertEquals(BotLocale.DEFAULT, result);
    }

    @Test
    void fromUpdate_messageIsNull_returnDefaultBotLocale() {
        when(update.message()).thenReturn(null);

        BotLocale result = BotLocale.fromUpdate(update);

        assertEquals(BotLocale.DEFAULT, result);
    }

    @Test
    void fromUpdate_userIsNull_returnDefaultBotLocale() {
        when(update.message()).thenReturn(message);
        when(message.from()).thenReturn(null);

        BotLocale result = BotLocale.fromUpdate(update);

        assertEquals(BotLocale.DEFAULT, result);
    }

    @Test
    void fromUpdate_languageCodeIsNull_returnDefaultBotLocale() {
        when(update.message()).thenReturn(message);
        when(message.from()).thenReturn(user);
        when(user.languageCode()).thenReturn(null);

        BotLocale result = BotLocale.fromUpdate(update);

        assertEquals(BotLocale.DEFAULT, result);
    }

    @ParameterizedTest
    @MethodSource(value = "provideRuLanguageCodes")
    void fromUpdate_ruLanguageCode_returnRuBotLocale(String languageCode) {
        when(update.message()).thenReturn(message);
        when(message.from()).thenReturn(user);
        when(user.languageCode()).thenReturn(languageCode);

        BotLocale result = BotLocale.fromUpdate(update);

        assertEquals(BotLocale.RU, result);
    }

    @ParameterizedTest
    @MethodSource(value = "provideUnspecifiedLanguageCodes")
    void fromUpdate_unspecifiedLanguageCode_returnDefaultBotLocale(String languageCode) {
        when(update.message()).thenReturn(message);
        when(message.from()).thenReturn(user);
        when(user.languageCode()).thenReturn(languageCode);

        BotLocale result = BotLocale.fromUpdate(update);

        assertEquals(BotLocale.DEFAULT, result);
    }

    private static Stream<String> provideRuLanguageCodes() {
        return Stream.of("ru", "RU", "Ru", "rU");
    }

    private static Stream<String> provideUnspecifiedLanguageCodes() {

        return Stream.of("ру", "en", "ua", "JP", "test");
    }

}
