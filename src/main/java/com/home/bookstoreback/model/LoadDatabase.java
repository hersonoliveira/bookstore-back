package com.home.bookstoreback.model;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Logger;

@Configuration
public class LoadDatabase {

    private static final Logger LOGGER = Logger.getLogger(LoadDatabase.class.getName());

    @Bean
    CommandLineRunner initDatabase(BookRepository repository) {
        return args -> {
            LOGGER.info("Preloading: " + repository.save(new Book("Book number 1","description 1")));
            LOGGER.info("Preloading: " + repository.save(new Book("Book number 2","description 2")));
        };
    }
}
