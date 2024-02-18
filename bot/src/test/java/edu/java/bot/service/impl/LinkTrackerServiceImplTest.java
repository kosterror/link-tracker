package edu.java.bot.service.impl;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import edu.java.bot.exception.IllegalCommandFormatException;
import edu.java.bot.exception.IllegalLinkFormatException;
import edu.java.bot.exception.LinkAlreadyTrackedException;
import edu.java.bot.exception.LinkNotFoundException;
import io.netty.util.internal.StringUtil;
import java.util.stream.Stream;
import org.apache.commons.validator.routines.UrlValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LinkTrackerServiceImplTest {

    private static final long USER_ID = 123;
    private static final String MESSAGE_1 = "/track link1";
    private static final String MESSAGE_2 = "/track link2";
    private static final String LINK_1 = "link1";
    private static final String LINK_2 = "link2";
    private static final String LIST_OF_LINKS = LINK_1 + "\n" + LINK_2 + "\n";
    @InjectMocks
    private LinkTrackerServiceImpl linkTrackerService;

    @Mock
    private UrlValidator urlValidator;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private User user;

    @ParameterizedTest
    @MethodSource(value = "provideCorrectMessages")
    void trackLink_updateWithUntrackedValidLink_linkTracked(String textMessage, String link) {
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn(textMessage);
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(USER_ID);
        when(urlValidator.isValid(anyString())).thenReturn(true);

        linkTrackerService.trackLink(update);

        verify(urlValidator, times(1)).isValid(link);
    }

    @Test
    void trackLink_updateWithInvalidLink_throwIllegalLinkFormatException() {
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn(MESSAGE_1);
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(USER_ID);
        when(urlValidator.isValid(anyString())).thenReturn(false);

        assertThrows(IllegalLinkFormatException.class, () -> linkTrackerService.trackLink(update));

        verify(urlValidator, times(1)).isValid(LINK_1);
    }

    @ParameterizedTest
    @MethodSource(value = "provideIncorrectMessages")
    void trackLink_updateWithInvalidCommandFormat_throwIllegalCommandFormatException(String textMessage) {
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn(textMessage);
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(USER_ID);

        assertThrows(IllegalCommandFormatException.class, () -> linkTrackerService.trackLink(update));

        verify(urlValidator, times(0)).isValid(anyString());
    }

    @Test
    void trackLink_updateWithUntrackedValidLinkTwoTimes_linkTrackedAndThenThrowLinkAlreadyTrackedException() {
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn(MESSAGE_1);
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(USER_ID);
        when(urlValidator.isValid(anyString())).thenReturn(true);

        linkTrackerService.trackLink(update);

        verify(urlValidator, times(1)).isValid(LINK_1);

        assertThrows(LinkAlreadyTrackedException.class, () -> linkTrackerService.trackLink(update));

        verify(urlValidator, times(2)).isValid(LINK_1);
    }

    @Test
    void untrackLink_updateWithTrackedLink_linkUntracked() {
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn(MESSAGE_1);
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(USER_ID);
        when(urlValidator.isValid(anyString())).thenReturn(true);

        linkTrackerService.trackLink(update);
        linkTrackerService.untrackLink(update);

        verify(urlValidator, times(1)).isValid(LINK_1);
    }

    @Test
    void untrackLink_updateWithUntrackedLink_throwLinkNotFoundException() {
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn(MESSAGE_1);
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(USER_ID);

        assertThrows(LinkNotFoundException.class, () -> linkTrackerService.untrackLink(update));
    }

    @Test
    void getTrackedLinks_userDoesNotHaveLink_returnEmptyString() {
        String expectedResult = StringUtil.EMPTY_STRING;
        String result = linkTrackerService.getTrackedLinks(USER_ID);

        assertEquals(expectedResult, result);
    }

    @Test
    void getTrackedLinks_userHaveTwoLinks_returnActualResult() {
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn(MESSAGE_1).thenReturn(MESSAGE_2);
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(USER_ID);
        when(urlValidator.isValid(anyString())).thenReturn(true);

        linkTrackerService.trackLink(update);
        linkTrackerService.trackLink(update);

        String result = linkTrackerService.getTrackedLinks(USER_ID);

        assertEquals(LIST_OF_LINKS, result);
    }

    private static Stream<Arguments> provideCorrectMessages() {
        return Stream.of(
            Arguments.of("/track link", "link"),
            Arguments.of(" /track word1", "word1"),
            Arguments.of("/track link2 ", "link2"),
            Arguments.of("  /track   link3 ", "link3"),
            Arguments.of("/track\nl1ink", "l1ink")
        );
    }

    private static Stream<String> provideIncorrectMessages() {
        return Stream.of(
            "/track",
            "/track qwe qwe",
            "/track qwe qwe qwe"
        );
    }

}
