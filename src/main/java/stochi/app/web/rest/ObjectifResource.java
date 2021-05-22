package stochi.app.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stochi.app.repository.ObjectifRepository;
import stochi.app.repository.UserRepository;
import stochi.app.service.ObjectifService;

@RestController
@RequestMapping("/api")
public class ObjectifResource {
    /*
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ObjectifService objectifService;
    private final ObjectifRepository objectifRepository;
    private final UserRepository userRepository;

    public ObjectifResource(
        ObjectifService objectifService,
        ObjectifRepository objectifRepository,
        UserRepository userRepository
    ) {
        this.objectifService = objectifService;
        this.objectifRepository = objectifRepository;
        this.userRepository = userRepository;
    }
    */

}
