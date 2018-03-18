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
import ru.ardecs.sideprojects.eventsourcing.model.Id;
import ru.ardecs.sideprojects.eventsourcing.repository.EventRepository;
import ru.ardecs.sideprojects.eventsourcing.service.EventStorageService;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
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

    public <T> T create(T entity, Class<? extends T> clazz) throws JsonProcessingException, IllegalArgumentException,
            InvocationTargetException, IllegalAccessException {

        LOG.info("Start process creating of " + clazz.getSimpleName());

        checkEntity(entity, clazz.getSimpleName());

        String id = getId(entity, clazz);
        if (StringUtils.isBlank(id)) {
            id = generationIdAndGetNameFieldEntity(entity, id);
        }

        if(!eventStorageService.isUnique(clazz.getSimpleName(), id, getNameIdField(entity))){
            throw new IllegalArgumentException("Field " + getNameIdField(entity) + " of " + clazz.getSimpleName() + " must be unique");
        };

        Map<String, String> map = Stream.of(entity.getClass().getDeclaredFields())
                .map(field -> retrieveNameAndValueField(field, entity))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(pair -> pair.getKey(), pair -> pair.getValue()));

        setUpdateDateToEntity(entity);

        saveEvent(clazz, id, EventType.CREATE, map);

        return entity;
    }

    private <T> String generationIdAndGetNameFieldEntity(T entity, String id) throws InvocationTargetException, IllegalAccessException {

        id = UUID.randomUUID().toString();
        String name = getNameIdField(entity);

        Stream.of(entity.getClass().getDeclaredMethods())
                .filter( method -> method.getName().equalsIgnoreCase("set" + name))
                .findFirst()
                .get().invoke(entity, id);
        return id;
    }

    private <T> String getNameIdField(T entity) {
        String nameFieldId = Stream.of(entity.getClass().getDeclaredMethods())
                .filter( method -> method.getAnnotation(Id.class) != null)
                .findFirst()
                .get()
                .getName();

        return nameFieldId.replace("get", "");
    }

    private <T> void checkEntity(T entity, String simpleName) {
        if (Objects.isNull(entity)) {
            LOG.info(simpleName + " is empty.");
            throw new IllegalArgumentException(simpleName + " is empty.");
        }
    }

    public <T> T update(T entity, Class<? extends T> clazz) throws JsonProcessingException, NoSuchElementException, IllegalArgumentException,
            InvocationTargetException, IllegalAccessException {

        LOG.info("Start process updating of " + clazz.getSimpleName());

        checkEntity(entity, clazz.getSimpleName());

        String id = getId(entity, clazz);

        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("Value of Id is empty.");
        }

        checkObjectIsExist(clazz, id);

        Date dateEvent = getDateLastEventOfEntity(entity);

        setUpdateDateToEntity(entity);

        Map<String, String> map = Stream.of(entity.getClass().getDeclaredFields())
                .map(field -> retrieveNameAndValueField(field, entity))
                .filter(Objects::nonNull)
                .filter(pair -> eventStorageService.isChanged(id, clazz.getSimpleName(), pair.getKey(), pair.getValue(), dateEvent))
                .collect(Collectors.toMap(pair -> pair.getKey(), pair -> pair.getValue()));

        saveEvent(clazz, id, EventType.UPDATE, map);

        return entity;
    }

    private <T> Date getDateLastEventOfEntity(T entity) throws IllegalAccessException, InvocationTargetException {

        return (Date) Stream.of(entity.getClass().getDeclaredMethods())
                .filter(method -> method.getName().equalsIgnoreCase("getCreatedDate"))
                .findFirst()
                .get().invoke(entity);
    }

    private <T> void setUpdateDateToEntity(T entity) throws IllegalAccessException, InvocationTargetException {
        Stream.of(entity.getClass().getDeclaredMethods())
                .filter(method -> method.getName().equalsIgnoreCase("setCreatedDate"))
                .findFirst().get().invoke(entity, new Date());
    }

    private <T> String getId(T entity, Class<? extends T> clazz) {
        return Stream.of(entity.getClass().getDeclaredMethods())
                .filter(field ->
                     (field.getAnnotation(Id.class) != null)
                )
                .map(method -> {
                    try {
                        return (String) method.invoke(entity);
                    } catch (IllegalAccessException e) {
                        LOG.error(e.getMessage());
                    } catch (InvocationTargetException e) {
                        LOG.error(e.getMessage());
                    }
                    return "";
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(new String());
    }

    private <T> void checkObjectIsExist(Class<? extends T> clazz, String id) throws NoSuchElementException {
        if (eventStorageService.isDeleted(id, clazz.getSimpleName())) {
            throw new NoSuchElementException(clazz.getSimpleName() + " with Id: " + id + " is not exist.");
        }
    }

    public <T> void delete(Class<? extends T> clazz, String id) throws JsonProcessingException {
        if (StringUtils.isBlank(id)) {
            LOG.info("Id of object can't be empty");
            return;
        }

        checkObjectIsExist(clazz, id);

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

        //TODO: This method must be replaced or deleting.
        if(field.getName().contains("createdDate")){
            return null;
        }

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
