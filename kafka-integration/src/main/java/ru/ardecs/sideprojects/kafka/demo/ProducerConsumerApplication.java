package ru.ardecs.sideprojects.kafka.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProducerConsumerApplication implements CommandLineRunner {

    @Autowired
    private Sender sender;

    public static void main(String[] args) {
        SpringApplication.run(ProducerConsumerApplication.class, args);
    }

    @Override
    public void run(String... strings) {
        sender.send("Spring Kafka Producer and Consumer Example");
    }
}