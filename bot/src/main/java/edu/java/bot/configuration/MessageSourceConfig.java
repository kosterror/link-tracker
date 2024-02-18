package edu.java.bot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class MessageSourceConfig {

    private static final String BASE_NAME = "messages";
    private static final String ENCODING = "UTF-8";

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename(BASE_NAME);
        source.setDefaultEncoding(ENCODING);
        return source;
    }

}
