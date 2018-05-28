package ru.ardecs.sideprojects.cqrs.query.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.ardecs.sideprojects.cqrs.query.storage.HeroesStorageService;
import ru.ardecs.sideprojects.eventsourcing.model.HeroEntity;


/**
 * @author <a href="mailto:srgeyilminskih@ardecs.com">Sergey Ilminskih</a>
 */
@RestController
@RequestMapping(value = "/cqrs")
public class HeroesQueryController {
    private final HeroesStorageService storeService;

    @Autowired
    public HeroesQueryController(HeroesStorageService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/")
    public String version() {
        return "it is test version: 1";
    }

    @GetMapping("/heroes")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<HeroEntity> getHeroes() {
        return storeService.getEntities();
    }

    @GetMapping("/heroes/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    public HeroEntity getHero(@PathVariable String id) {
        return storeService.getEntityById(id);
    }
}
