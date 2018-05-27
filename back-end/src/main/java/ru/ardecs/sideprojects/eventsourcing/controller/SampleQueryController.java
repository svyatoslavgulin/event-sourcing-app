package ru.ardecs.sideprojects.eventsourcing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.ardecs.sideprojects.eventsourcing.model.HeroEntity;
import ru.ardecs.sideprojects.eventsourcing.service.TemporaryStoreService;

import java.util.List;

/**
 * @author <a href="mailto:srgeyilminskih@ardecs.com">Sergey Ilminskih</a>
 */
@RestController
public class SampleQueryController {
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
}
