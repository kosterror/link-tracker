package edu.java.bot.util.i18n;

import java.util.Locale;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.ResourceBundleMessageSource;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Тесты для {@link MessageResolver}.
 */
@ExtendWith(MockitoExtension.class)
class MessageResolverTest {

    private static final Object[] ARGUMENTS = {"arg1", "arg2"};

    @Mock
    private ResourceBundleMessageSource resourceSource;

    @InjectMocks
    private MessageResolver messageResolver;

    @ParameterizedTest
    @MethodSource("provideMessageKeysAndLocales")
    void resolve_validMessageKeyAndLocale_callsGetMessageWithCorrectParameters(MessageKey key, BotLocale locale) {
        String expectedCode = key.getCode();
        Locale expectedLocale = Locale.forLanguageTag(locale.getValue());

        messageResolver.resolve(key, locale, ARGUMENTS);

        verify(resourceSource, times(1)).getMessage(expectedCode, ARGUMENTS, expectedLocale);
    }

    private static Stream<Arguments> provideMessageKeysAndLocales() {
        return Stream.of(
            Arguments.of(MessageKey.COMMANDS_START_DESCRIPTION, BotLocale.RU),
            Arguments.of(MessageKey.COMMANDS_TRACK_ANSWER, BotLocale.DEFAULT)
        );
    }

}
