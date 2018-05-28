package ru.ardecs.sideprojects.eventsourcing.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ardecs.sideprojects.eventsourcing.handlers.CommonHandler;
import ru.ardecs.sideprojects.eventsourcing.model.HeroEntity;
import ru.ardecs.sideprojects.eventsourcing.service.TemporaryStoreService;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author <a href="mailto:srgeyilminskih@ardecs.com">Sergey Ilminskih</a>
 */
@RestController
public class SampleController {

    private static final Logger LOG = LoggerFactory.getLogger(SampleController.class);

    @Autowired
    private CommonHandler<HeroEntity> commonHandler;
    @Autowired
    private TemporaryStoreService storeService;

    @GetMapping("/")
    public String version() {
        return "it is test version: 1";
    }

    @GetMapping("/heroes")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<HeroEntity> getHeroes() {
        return storeService.getEntitys();
    }

    @GetMapping("/heroes/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    public HeroEntity getHeroes(@PathVariable String id) {
        return storeService.getEntityById(id);
    }

    @PostMapping("/heroes")
    @CrossOrigin(origins = "http://localhost:4200")
    public HeroEntity save(@RequestBody HeroEntity body) throws JsonProcessingException, InvocationTargetException, IllegalAccessException {
        HeroEntity entity = commonHandler.create(body, HeroEntity.class);
        storeService.save(entity);
        return entity;
    }

    @PutMapping("/heroes")
    @CrossOrigin(origins = "http://localhost:4200")
    public HeroEntity updateHeroes(@RequestBody HeroEntity body) throws JsonProcessingException, InvocationTargetException, IllegalAccessException {
        LOG.info("Body id: " + body.getId());
        LOG.info("Body name: " + body.getName());
        HeroEntity entity = commonHandler.update(body, HeroEntity.class);
        return storeService.update(entity);
    }

    @DeleteMapping("/heroes/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    public void deleteHeroe(@PathVariable String id) throws JsonProcessingException {
        storeService.delete(id);
        commonHandler.delete(HeroEntity.class, id);
    }
}
