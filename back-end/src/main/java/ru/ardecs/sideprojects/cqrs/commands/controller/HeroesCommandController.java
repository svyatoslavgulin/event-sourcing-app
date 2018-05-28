package ru.ardecs.sideprojects.cqrs.commands.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ardecs.sideprojects.eventsourcing.handlers.CommonHandler;
import ru.ardecs.sideprojects.eventsourcing.model.HeroEntity;

import java.lang.reflect.InvocationTargetException;

/**
 * @author <a href="mailto:srgeyilminskih@ardecs.com">Sergey Ilminskih</a>
 */
@RestController
@RequestMapping(value = "/cqrs")
public class HeroesCommandController {
    private static final Logger LOG = LoggerFactory.getLogger(HeroesCommandController.class);

    private final CommonHandler<HeroEntity> commonHandler;

    @Autowired
    public HeroesCommandController(CommonHandler<HeroEntity> commonHandler) {
        this.commonHandler = commonHandler;
    }

    @PostMapping("/heroes")
    @CrossOrigin(origins = "http://localhost:4200")
    public void saveHero(@RequestBody HeroEntity body) throws JsonProcessingException, InvocationTargetException, IllegalAccessException {
        commonHandler.create(body, HeroEntity.class);
    }

    @PutMapping("/heroes")
    @CrossOrigin(origins = "http://localhost:4200")
    public void updateHero(@RequestBody HeroEntity body) throws JsonProcessingException, InvocationTargetException, IllegalAccessException {
        LOG.info("Body id: " + body.getId());
        LOG.info("Body name: " + body.getName());
        commonHandler.update(body, HeroEntity.class);
    }

    @DeleteMapping("/heroes/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    public void deleteHero(@PathVariable String id) throws JsonProcessingException {
        commonHandler.delete(HeroEntity.class, id);
    }
}
