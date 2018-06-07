package ru.ardecs.sideprojects.cqrs.query.storage.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ardecs.sideprojects.cqrs.query.storage.model.HeroQueryEntity;

@Repository
public interface HeroQueryRepository extends CrudRepository<HeroQueryEntity, String> {
}
