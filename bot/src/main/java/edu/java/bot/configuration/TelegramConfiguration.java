package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Класс с конфигурацией для бота.
 */
@Configuration
@RequiredArgsConstructor
public class TelegramConfiguration {

    private final ApplicationProperties properties;

    /**
     * Создает бин {@link TelegramBot}.
     *
     * @return бин {@link TelegramBot}
     */
    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot.Builder(properties.telegramToken())
            .build();
    }

}
