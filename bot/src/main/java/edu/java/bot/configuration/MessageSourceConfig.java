package edu.java.bot.configuration;

import edu.java.bot.util.i18n.BotLocale;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Slf4j
@Configuration
public class MessageSourceConfig {

    private static final String BASE_NAME = "messages";
    private static final String ENCODING = "UTF-8";

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename(BASE_NAME);
        source.setDefaultEncoding(ENCODING);
        source.setDefaultLocale(Locale.forLanguageTag(BotLocale.DEFAULT.getValue()));
        return source;
    }

}
