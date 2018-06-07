package ru.ardecs.sideprojects.cqrs.query.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ardecs.sideprojects.cqrs.query.storage.model.HeroQueryEntity;
import ru.ardecs.sideprojects.cqrs.query.storage.repository.HeroQueryRepository;

import java.util.Optional;


/**
 * @author <a href="mailto:srgeyilminskih@ardecs.com">Sergey Ilminskih</a>
 */
@RestController
@RequestMapping(value = "/cqrs")
public class HeroesQueryController {
    private final HeroQueryRepository heroQueryRepository;

    @Autowired
    public HeroesQueryController(HeroQueryRepository heroQueryRepository) {
        this.heroQueryRepository = heroQueryRepository;
    }

    @GetMapping("/")
    public String version() {
        return "it is test version: 1";
    }

    @GetMapping("/heroes")
    @CrossOrigin(origins = "http://localhost:4200")
    public Iterable<HeroQueryEntity> getHeroes() {
        return heroQueryRepository.findAll();
    }

    @GetMapping("/heroes/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    public Optional<HeroQueryEntity> getHero(@PathVariable String id) {
        return heroQueryRepository.findById(id);
    }
}
