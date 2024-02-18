package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;

public interface LinkTrackerService {

    void trackLink(Update update);

}
