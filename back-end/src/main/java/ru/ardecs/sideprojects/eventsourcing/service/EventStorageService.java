package ru.ardecs.sideprojects.eventsourcing.service;

import org.springframework.stereotype.Component;

@Component
public class EventStorageService {

    /**
     * @param objectId    object identifier
     * @param objectClass object type/class name
     * @param fieldName   checked field name of the objectClass
     * @param fieldValue  new value of the fieldName
     * @return is field value was changed
     */
    public boolean isChanged(String objectId, String objectClass, String fieldName, String fieldValue) {
        return true;
    }
}
