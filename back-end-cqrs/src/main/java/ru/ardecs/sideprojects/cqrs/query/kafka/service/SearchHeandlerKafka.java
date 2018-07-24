package ru.ardecs.sideprojects.cqrs.query.kafka.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.ardecs.sideprojects.cqrs.commands.kafka.sender.model.Event;

import java.io.IOException;
import java.util.*;

@Service
public class SearchHeandlerKafka {

    private static final Logger LOG = LoggerFactory.getLogger(SearchHeandlerKafka.class);

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${app.topic.hero-events}")
    private String topic;

    private ObjectMapper mapper = new ObjectMapper();

    public Event getEventsFromKafkaById(String id) {
        Date startSearchDate = new Date();

        Consumer<String, String> consumer = getConsumer();

        Boolean continueSearch = Boolean.TRUE;
        int startPosition = 0;
        while (continueSearch) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            consumer.seek(getTopicPartition(consumer), startPosition);

            for (ConsumerRecord<String, String> record : records) {
                LOG.info("offset = " + record.offset() + ", key = " + record.key() + ", value = " + record.value());
                Event event = getEvent(record.value());
                if (startSearchDate.before(event.getCreatedDate())) {
                    continueSearch = finish(consumer);
                    break;
                } else if (event.getId().equals(id)) {
                    continueSearch = finish(consumer);
                    return event;
                }
            }

            if (records.count() == Integer.MAX_VALUE) {
                startPosition = startPosition + Integer.MAX_VALUE;
            } else if (continueSearch) {
                continueSearch = finish(consumer);
            }
        }

        return null;
    }

    public List<Event> getAllEventsFromKafkaByObjectId(String id, int offset) {
        Date startSearchDate = new Date();

        Consumer<String, String> consumer = new KafkaConsumer<>(getPropertiesOfConsumer());
        TopicPartition topicPartition = new TopicPartition(topic, 0);
        List<TopicPartition> topics = Arrays.asList(topicPartition);
        consumer.assign(topics);
        consumer.seek(topicPartition, offset);
        List<Event> allEvents = new ArrayList<>();
        Boolean continueSearch = Boolean.TRUE;
        int startPosition = offset;
        while (continueSearch) {
            ConsumerRecords<String, String> records = consumer.poll(100);

            for (ConsumerRecord<String, String> record : records) {
                Event event = getEvent(record.value());
                if (startSearchDate.before(event.getCreatedDate())) {
                    continueSearch = finish(consumer);
                    break;
                } else if (event.getObjectId() != null && event.getObjectId().equals(id)) {
                    allEvents.add(event);
                }
                LOG.info("offset = " + record.offset() + ", key = " + record.key() + ", value = " + record.value());
            }

            if (records != null && !records.isEmpty() && records.count() == Integer.MAX_VALUE) {
                startPosition = startPosition + Integer.MAX_VALUE;
            } else if (continueSearch) {
                continueSearch = finish(consumer);
            }
        }

        return allEvents;
    }

    public List<Event> getAllEvents() {
        Date startSearchDate = new Date();

        Consumer<String, String> consumer = getConsumer();
        List<Event> allEvents = new ArrayList<>();
        Boolean continueSearch = Boolean.TRUE;
        int startPosition = 0;
        while (continueSearch) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            consumer.seek(getTopicPartition(consumer), startPosition);

            for (ConsumerRecord<String, String> record : records) {
                Event event = getEvent(record.value());
                if (startSearchDate.before(event.getCreatedDate())) {
                    continueSearch = finish(consumer);
                    break;
                }
                allEvents.add(event);
                LOG.info("offset = " + record.offset() + ", key = " + record.key() + ", value = " + record.value());
            }

            if (records != null && !records.isEmpty() && records.count() == Integer.MAX_VALUE) {
                startPosition = startPosition + Integer.MAX_VALUE;
            } else if (continueSearch) {
                continueSearch = finish(consumer);
            }
        }

        return allEvents;
    }


    private Event getEvent(String msg) {
        try {
            return mapper.readValue(msg, Event.class);
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
        return new Event();
    }

    private Consumer<String, String> getConsumer() {
        Consumer<String, String> consumer = new KafkaConsumer<>(getPropertiesOfConsumer());
        consumer.subscribe(Arrays.asList(topic));
        return consumer;
    }

    private TopicPartition getTopicPartition(Consumer consumer) {
        List<PartitionInfo> list = consumer.partitionsFor(topic);
        return new TopicPartition(list.get(0).topic(), list.get(0).partition());
    }

    private Boolean finish(Consumer<String, String> consumer) {
        consumer.close();
        return Boolean.FALSE;
    }

    private Properties getPropertiesOfConsumer() {

        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put("group.id", "G1");

        return props;
    }
}
