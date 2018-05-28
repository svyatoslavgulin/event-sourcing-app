package ru.ardecs.sideprojects.cqrs.commands.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.ardecs.sideprojects.eventsourcing.model.HeroEntity;

/**
 * @author <a href="mailto:srgeyilminskih@ardecs.com">Sergey Ilminskih</a>
 */
@RestController
@RequestMapping(value = "/cqrs")
public class HeroesCommandController {
    private static final Logger LOG = LoggerFactory.getLogger(HeroesCommandController.class);

    @PostMapping("/heroes")
    @CrossOrigin(origins = "http://localhost:4200")
    public void saveHero(@RequestBody HeroEntity body) {
        //TODO: do something
    }

    @PutMapping("/heroes")
    @CrossOrigin(origins = "http://localhost:4200")
    public void updateHero(@RequestBody HeroEntity body) {
        LOG.info("Body id: " + body.getId());
        LOG.info("Body name: " + body.getName());
        //TODO: do something
    }

    @DeleteMapping("/heroes/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    public void deleteHero(@PathVariable String id) {
        //TODO: do something
    }
}
