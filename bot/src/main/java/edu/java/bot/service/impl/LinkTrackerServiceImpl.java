package edu.java.bot.service.impl;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.exception.IllegalCommandFormatException;
import edu.java.bot.exception.IllegalLinkFormatException;
import edu.java.bot.exception.LinkAlreadyTrackedException;
import edu.java.bot.exception.LinkNotFoundException;
import edu.java.bot.service.LinkTrackerService;
import io.netty.util.internal.StringUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Service;

/**
 * Реализация {@link LinkTrackerService}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LinkTrackerServiceImpl implements LinkTrackerService {

    private final Map<Long, Set<String>> links = new HashMap<>();
    private final UrlValidator urlValidator;

    @Override
    public void trackLink(Update update) {
        Message message = update.message();
        long tgUserId = message.from().id();
        String link = extractAndValidateLink(message.text());
        Set<String> userLinks = links.getOrDefault(tgUserId, new TreeSet<>());
        if (userLinks.contains(link)) {
            throw new LinkAlreadyTrackedException(String.format("Link %s already tracked", link));
        }
        userLinks.add(link);
        links.put(tgUserId, userLinks);
        log.info("Started tracking link {} ", link);
    }

    @Override
    public void untrackLink(Update update) {
        Message message = update.message();
        long tgUserId = message.from().id();
        String link = extractLink(message.text());
        Set<String> userLinks = links.get(tgUserId);

        if (userLinks == null || !userLinks.contains(link)) {
            throw new LinkNotFoundException(String.format(
                "Link %s not found on the user with tg id %s",
                link,
                tgUserId
            ));
        }

        userLinks.remove(link);
    }

    @Override
    public String getTrackedLinks(long tgUserId) {
        Set<String> linksSet = links.get(tgUserId);

        if (linksSet == null || linksSet.isEmpty()) {
            return StringUtil.EMPTY_STRING;
        }

        StringBuilder stringBuilder = new StringBuilder();
        linksSet.forEach(link -> {
            stringBuilder.append(link);
            stringBuilder.append("\n");
        });

        return stringBuilder.toString();
    }

    private String extractAndValidateLink(String message) {
        String link = extractLink(message);

        if (!urlValidator.isValid(link)) {
            throw new IllegalLinkFormatException("The link is invalid");
        }

        return link;
    }

    private String extractLink(String message) {
        String[] splitMessage = message.trim().split("\\s+");

        if (splitMessage.length != 2) {
            throw new IllegalCommandFormatException("The command format is incorrect");
        }

        return splitMessage[1];
    }

}
