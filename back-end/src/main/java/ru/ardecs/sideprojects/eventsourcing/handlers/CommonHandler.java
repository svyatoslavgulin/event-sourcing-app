package ru.ardecs.sideprojects.eventsourcing.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ardecs.sideprojects.eventsourcing.model.Event;
import ru.ardecs.sideprojects.eventsourcing.model.EventType;
import ru.ardecs.sideprojects.eventsourcing.repository.EventRepository;
import ru.ardecs.sideprojects.eventsourcing.service.EventStorageService;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:sergey.ilminskih@ardecs.com">Sergey Ilminskih</a>
 */
@Component
public class CommonHandler<T> {

    private static final Logger LOG = LoggerFactory.getLogger(CommonHandler.class);

    @Autowired
    private EventStorageService eventStorageService;

    @Autowired
    private EventRepository eventRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    public <T> T create(T entity, Class<? extends T> clazz, String id) throws JsonProcessingException {

        LOG.info("Start process creating of " + clazz.getSimpleName());

        if (Objects.isNull(entity)) {
            LOG.info(clazz.getSimpleName() + " is empty.");
            return null;
        }

        Map<String, String> map = Stream.of(entity.getClass().getDeclaredFields())
                .map(field -> retrieveNameAndValueField(field, entity))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(pair -> pair.getKey(), pair -> pair.getValue()));

        saveEvent(clazz, id, EventType.CREATE, map);

        return entity;
    }

    public <T> T update(T entity, Class<? extends T> clazz, String id) throws JsonProcessingException {

        LOG.info("Start process updating of " + clazz.getSimpleName());

        if (Objects.isNull(entity)) {
            LOG.info(clazz.getSimpleName() + " is empty.");
            return null;
        }

        Map<String, String> map = Stream.of(entity.getClass().getDeclaredFields())
                .map(field -> retrieveNameAndValueField(field, entity))
                .filter(Objects::nonNull)
                //TODO: this method which will say that entity is deleted or not.
                .filter(pair -> eventStorageService.isChanged(id, clazz.getSimpleName(), pair.getKey(), pair.getValue()))
                .collect(Collectors.toMap(pair -> pair.getKey(), pair -> pair.getValue()));

        saveEvent(clazz, id, EventType.UPDATE, map);

        return entity;
    }

    public <T> void delete(Class<? extends T> clazz, String id) throws JsonProcessingException {
        if (StringUtils.isBlank(id)) {
            LOG.info("Indetification of object can't be empty");
            return;
        }

        //TODO: this method which will say that entity is deleted or not.
        saveEvent(clazz, id, EventType.REMOVE, null);
    }

    private <T> void saveEvent(Class<? extends T> clazz, String id,
                               EventType eventType, Map<String, String> map) throws JsonProcessingException {

        if (Objects.nonNull(map) && map.size() != 0 || eventType.equals(EventType.REMOVE)) {
            Event event = new Event();
            event.setType(eventType);
            event.setName(clazz.getSimpleName());
            event.setObjectId(id);
            event.setPayload(map);
            event.setCreatedDate(new Date());

            eventRepository.save(event);
        }
    }

    /**
     * @param field
     * @param entity
     * @param <T>
     * @return pare containing fieldName is key and fieldValue is value.
     */
    private <T> Pair<String, String> retrieveNameAndValueField(Field field, T entity) {

        for (Method method : entity.getClass().getMethods()) {
            if (isGetMethodCurrentField(field, method)) {
                try {
                    if (method.invoke(entity) != null) {
                        return new MutablePair<>(field.getName(), (String) method.invoke(entity));
                    }
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
}
