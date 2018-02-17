package ru.ardecs.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:srgeyilminskih@ardecs.com">Sergey Ilminskih</a>
 */
@RestController
public class SampleController {

    @GetMapping("/")
    public String version() {
        return "it is test version: 1";
    }
}
