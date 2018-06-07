package ru.ardecs.sideprojects.cqrs.commands.controller;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ObjectUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ObjectUtils.class);

    public static <T> Map<String, String> getPropertiesMap(T entity) {
        return Stream.of(entity.getClass().getDeclaredFields())
                .map(field -> retrieveNameAndValueField(field, entity))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(pair -> pair.getKey(), pair -> pair.getValue()));
    }

    /**
     * @param field
     * @param entity
     * @param <T>
     * @return pare containing fieldName is key and fieldValue is value.
     */
    private static <T> Pair<String, String> retrieveNameAndValueField(Field field, T entity) {

        //TODO: This method must be replaced or deleting.
        if (field.getName().contains("createdDate")) {
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

    private static boolean isGetMethodCurrentField(Field field, Method method) {
        return method.getName().toLowerCase().contains(field.getName().toLowerCase())
                && method.getName().contains("get");
    }
}
