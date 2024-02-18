package edu.java.bot.configuration;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Класс с основными конфигурационными параметрами приложения.
 * @param telegramToken токен для бота телеграмма.
 */
@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationProperties(
    @NotEmpty
    String telegramToken
) {
}
