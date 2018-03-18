package ru.ardecs.sideprojects.eventsourcing.service;

import java.util.Date;
import java.util.List;

import com.mongodb.MongoClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import ru.ardecs.sideprojects.eventsourcing.model.Event;
import ru.ardecs.sideprojects.eventsourcing.model.EventType;

@Component
public class EventStorageService {

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    /**
     * @param objectId    object identifier
     * @param objectClass object type/class name
     * @param fieldName   checked field name of the objectClass
     * @param fieldValue  new value of the fieldName
     * @return is field value was changed
     */
    public boolean isChanged(String objectId, String objectClass, String fieldName, String fieldValue, Date create) throws IllegalArgumentException {
        Event lastEventWithChangedField = getLastEventWithUpdatedField(objectId, objectClass, fieldName);

        if(lastEventWithChangedField != null && create.before(lastEventWithChangedField.getCreatedDate())){
            throw new IllegalArgumentException(fieldName + " has been updated " + lastEventWithChangedField.getCreatedDate()
                    + " when this update " + create);
        }

        if (lastEventWithChangedField == null) {
            return false;
        }

        String actualValue = lastEventWithChangedField.getPayload().get(fieldName);
        return actualValue != null && !actualValue.equals(fieldValue);
    }

    private Event getLastEventWithUpdatedField(String objectId, String objectClass, String fieldName) {
        MongoOperations mongoOps = new MongoTemplate(new SimpleMongoDbFactory(new MongoClient(), databaseName));

        Query query = Query.query(Criteria.where("payload." + fieldName).exists(true));
        query.addCriteria(Criteria.where("objectId").is(objectId));
        query.addCriteria(Criteria.where("name").is(objectClass));
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "createdDate")));

        List<Event> events = mongoOps.find(query, Event.class);

        return ((events != null) && (!events.isEmpty())) ? events.get(0) : null;
    }

    public boolean isDeleted(String objectId, String objectClass) {
        MongoOperations mongoOps = new MongoTemplate(new SimpleMongoDbFactory(new MongoClient(), databaseName));

        Query query = Query.query(Criteria.where("type").is(EventType.REMOVE));
        query.addCriteria(Criteria.where("objectId").is(objectId));
        query.addCriteria(Criteria.where("name").is(objectClass));

        return mongoOps.count(query, Event.class) > 0;
    }

    public boolean isUnique(String objectClass, String objectId, String fieldName){
        MongoOperations mongoOps = new MongoTemplate(new SimpleMongoDbFactory(new MongoClient(), databaseName));

        Query query = Query.query(Criteria.where("objectId").is(objectId));
        query.addCriteria(Criteria.where("name").is(objectClass));

        List<Event> events = mongoOps.find(query, Event.class);

        return events == null || events.isEmpty();
    }
}
