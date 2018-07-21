package ru.ardecs.sideprojects.cqrs.query.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ardecs.sideprojects.cqrs.commands.kafka.sender.model.Event;
import ru.ardecs.sideprojects.cqrs.query.storage.model.HeroQueryEntity;
import ru.ardecs.sideprojects.cqrs.query.storage.repository.HeroQueryRepository;
import ru.ardecs.sideprojects.cqrs.query.kafka.service.SearchHeandlerKafka;

import java.util.List;
import java.util.Optional;


/**
 * @author <a href="mailto:srgeyilminskih@ardecs.com">Sergey Ilminskih</a>
 */
@RestController
@RequestMapping(value = "/cqrs")
public class HeroesQueryController {
    private final HeroQueryRepository heroQueryRepository;
    private final SearchHeandlerKafka searchHeandlerKafka;

    @Autowired
    public HeroesQueryController(SearchHeandlerKafka searchHeandlerKafka, HeroQueryRepository heroQueryRepository) {
        this.heroQueryRepository = heroQueryRepository;
        this.searchHeandlerKafka =searchHeandlerKafka;
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

    @GetMapping("/event")
    @CrossOrigin(origins = "http://localhost:4200")
    public Event getEvent(@RequestParam String id) {
        return searchHeandlerKafka.getEventsFromKafkaById(id);
    }

    @GetMapping("/events")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Event> getAllEventForObjects(@RequestParam String objectId, @RequestParam(defaultValue = "0", required = false) int offset) {
        return searchHeandlerKafka.getAllEventsFromKafkaByObjectId(objectId, offset);
    }

    @GetMapping("/allevents")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Event> getAllEvent() {
        return searchHeandlerKafka.getAllEvents();
    }
}
