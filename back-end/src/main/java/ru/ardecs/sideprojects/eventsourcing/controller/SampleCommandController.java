package ru.ardecs.sideprojects.eventsourcing.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.ardecs.sideprojects.eventsourcing.handlers.CommonHandler;
import ru.ardecs.sideprojects.eventsourcing.model.HeroEntity;
import ru.ardecs.sideprojects.eventsourcing.service.TemporaryStoreService;

import java.lang.reflect.InvocationTargetException;

/**
 * @author <a href="mailto:srgeyilminskih@ardecs.com">Sergey Ilminskih</a>
 */
@RestController
public class SampleCommandController {

    private static final Logger LOG = LoggerFactory.getLogger(SampleCommandController.class);

    @Autowired
    private CommonHandler<HeroEntity> commonHandler;
    @Autowired
    private TemporaryStoreService storeService;

    @PostMapping("/heroes")
    @CrossOrigin(origins = "http://localhost:4200")
    public HeroEntity saveHero(@RequestBody HeroEntity body) throws JsonProcessingException, InvocationTargetException, IllegalAccessException {
        HeroEntity entity = commonHandler.create(body, HeroEntity.class);
        storeService.save(entity);
        return entity;
    }

    @PutMapping("/heroes")
    @CrossOrigin(origins = "http://localhost:4200")
    public HeroEntity updateHero(@RequestBody HeroEntity body) throws JsonProcessingException, InvocationTargetException, IllegalAccessException {
        LOG.info("Body id: " + body.getId());
        LOG.info("Body name: " + body.getName());
        HeroEntity entity = commonHandler.update(body, HeroEntity.class);
        return storeService.update(entity);
    }

    @DeleteMapping("/heroes/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    public void deleteHero(@PathVariable String id) throws JsonProcessingException {
        storeService.delete(id);
        commonHandler.delete(HeroEntity.class, id);
    }
}
