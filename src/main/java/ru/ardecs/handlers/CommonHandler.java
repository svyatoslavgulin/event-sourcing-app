package ru.ardecs.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ardecs.service.TemporaryStoreService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:sergey.ilminskih@ardecs.com">Sergey Ilminskih</a>
 */
@Component
public class CommonHandler<T> {

    @Autowired
    private TemporaryStoreService storeService;

    public boolean update(T entity) {
        List<String> filds = new ArrayList<>();
        
        return true;
    }
}
