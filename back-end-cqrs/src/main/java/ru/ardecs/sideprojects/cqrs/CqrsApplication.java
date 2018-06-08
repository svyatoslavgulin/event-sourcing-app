package ru.ardecs.sideprojects.cqrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"ru.ardecs.sideprojects.cqrs.query.storage.repository"})
public class CqrsApplication implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(CqrsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CqrsApplication.class, args);
    }

    @Override
    public void run(String... strings) {
        LOG.info("Application started");
    }
}