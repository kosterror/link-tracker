package edu.java.bot;

import edu.java.bot.configuration.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Главный класс приложения.
 */
@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class BotApplication {

    /**
     * Точка входа в приложение.
     *
     * @param args параметры.
     */
    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
    }
}
