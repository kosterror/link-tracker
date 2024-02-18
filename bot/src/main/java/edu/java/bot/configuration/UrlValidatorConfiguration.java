package edu.java.bot.configuration;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Класс конфигурации для {@link UrlValidator}.
 */
@Configuration
public class UrlValidatorConfiguration {

    /**
     * Создает бин класса {@link UrlValidator}.
     *
     * @return бин.
     */
    @Bean
    public UrlValidator urlValidator() {
        return new UrlValidator();
    }

}
