package ru.ardecs.sideprojects.eventsourcing.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:sergey.ilminskih@ardecs.com">Sergey Ilminskih</a>
 */
@Component
public class CommonHandler<T> {

    private static final Logger LOG = LoggerFactory.getLogger(CommonHandler.class);

    private final ObjectMapper mapper = new ObjectMapper();

    public <T> T update(String body, Class<? extends T> clazz, String id) {

        LOG.info("Start process updating of " + clazz.getTypeName());
        T entity = getEntity(body, clazz);

        if (Objects.isNull(entity)) {
            LOG.info(clazz.getTypeName() + " is empty.");
            return entity;
        }

        Stream.of(entity.getClass().getDeclaredFields())
                .map(field -> retrieveNameAndValueField(field, entity))
                .filter(Objects::nonNull)
                .forEach(pair -> {
                    LOG.info("Here, I must get result of execution Slava's classes");
                });

        return entity;
    }

    private <T> Pair<String, String> retrieveNameAndValueField(Field field, T entity) {

        for (Method method : entity.getClass().getMethods()) {
            if (isGetMethodCurrentField(field, method)) {
                try {
                    return new MutablePair<>(field.getName(), (String) method.invoke(entity));
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }

        LOG.info("Could not get the value of the field " + field.getName());
        return null;
    }

    private boolean isGetMethodCurrentField(Field field, Method method) {
        return method.getName().toLowerCase().contains(field.getName().toLowerCase())
                && method.getName().contains("get");
    }

    private <T> T getEntity(String body, Class<? extends T> clazz) throws IllegalArgumentException {

        try {
            return mapper.readValue(body, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
