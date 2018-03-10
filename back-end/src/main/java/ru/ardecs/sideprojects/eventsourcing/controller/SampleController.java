package ru.ardecs.sideprojects.eventsourcing.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ardecs.sideprojects.eventsourcing.handlers.CommonHandler;
import ru.ardecs.sideprojects.eventsourcing.model.EventType;
import ru.ardecs.sideprojects.eventsourcing.model.TestEntity;
import ru.ardecs.sideprojects.eventsourcing.service.TemporaryStoreService;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:srgeyilminskih@ardecs.com">Sergey Ilminskih</a>
 */
@RestController
public class SampleController {

    private static final Logger LOG = LoggerFactory.getLogger(SampleController.class);

    @Autowired
    private CommonHandler<TestEntity> commonHandler;
    @Autowired
    private TemporaryStoreService storeService;

    private AtomicInteger counter = new AtomicInteger(4);

    @GetMapping("/")
    public String version() {
        return "it is test version: 1";
    }

    @GetMapping("/heroes")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<TestEntity> getHeroes() {
        return storeService.getEntitys();
    }

    @GetMapping("/heroes/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    public TestEntity getHeroes(@PathVariable String id) {
        return storeService.getEntityById(id);
    }

    @PostMapping("/heroes")
    @CrossOrigin(origins = "http://localhost:4200")
    public TestEntity save(@RequestBody TestEntity body) throws JsonProcessingException {
        String id = String.valueOf(counter.incrementAndGet());
        body.setId(id);
        TestEntity entity = commonHandler.create(body, TestEntity.class, id);
        storeService.save(entity);
        return entity;
    }

    @PutMapping("/heroes")
    @CrossOrigin(origins = "http://localhost:4200")
    public TestEntity updateHeroes(@RequestBody TestEntity body) throws JsonProcessingException {
        LOG.info("Body id: " + body.getId());
        LOG.info("Body name: " + body.getName());
        TestEntity entity = commonHandler.update(body, TestEntity.class, body.getId());
        return storeService.update(entity);
    }

    @DeleteMapping("/heroes/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    public void deleteHeroe(@PathVariable String id) throws JsonProcessingException {
        storeService.delete(id);
        commonHandler.delete(EventType.class, id);
    }
}
