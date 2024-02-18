package edu.java.bot.service.impl;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.exception.IllegalCommandFormatException;
import edu.java.bot.exception.IllegalLinkFormatException;
import edu.java.bot.exception.LinkAlreadyTrackedException;
import edu.java.bot.service.LinkTrackerService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Service;

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
        Set<String> userLinks = links.getOrDefault(tgUserId, new HashSet<>());
        if (userLinks.contains(link)) {
            throw new LinkAlreadyTrackedException(String.format("Link %s already tracked", link));
        }
        userLinks.add(link);
        links.put(tgUserId, userLinks);
        log.info("Started tracking link {} ", link);
    }

    private String extractAndValidateLink(String message) {
        String[] splitMessage = message.trim().split(" ");

        if (splitMessage.length != 2) {
            throw new IllegalCommandFormatException("The command format is incorrect");
        }

        String link = splitMessage[1];

        if (!urlValidator.isValid(link)) {
            throw new IllegalLinkFormatException("The link is invalid");
        }

        return link;
    }

}
