package stochi.app.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import stochi.app.domain.ApplicationUser;
import stochi.app.repository.ApplicationUserRepository;
import stochi.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link stochi.app.domain.ApplicationUser}.
 */
@RestController
@RequestMapping("/api")
public class ApplicationUserResource {

    private final Logger log = LoggerFactory.getLogger(ApplicationUserResource.class);

    private static final String ENTITY_NAME = "applicationUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApplicationUserRepository applicationUserRepository;

    public ApplicationUserResource(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    /**
     * {@code POST  /application-users} : Create a new applicationUser.
     *
     * @param applicationUser the applicationUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new applicationUser, or with status {@code 400 (Bad Request)} if the applicationUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/application-users")
    public ResponseEntity<ApplicationUser> createApplicationUser(@RequestBody ApplicationUser applicationUser) throws URISyntaxException {
        log.debug("REST request to save ApplicationUser : {}", applicationUser);
        if (applicationUser.getId() != null) {
            throw new BadRequestAlertException("A new applicationUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApplicationUser result = applicationUserRepository.save(applicationUser);
        return ResponseEntity
            .created(new URI("/api/application-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /application-users/:id} : Updates an existing applicationUser.
     *
     * @param id the id of the applicationUser to save.
     * @param applicationUser the applicationUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationUser,
     * or with status {@code 400 (Bad Request)} if the applicationUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the applicationUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/application-users/{id}")
    public ResponseEntity<ApplicationUser> updateApplicationUser(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ApplicationUser applicationUser
    ) throws URISyntaxException {
        log.debug("REST request to update ApplicationUser : {}, {}", id, applicationUser);
        if (applicationUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicationUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicationUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ApplicationUser result = applicationUserRepository.save(applicationUser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, applicationUser.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /application-users/:id} : Partial updates given fields of an existing applicationUser, field will ignore if it is null
     *
     * @param id the id of the applicationUser to save.
     * @param applicationUser the applicationUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationUser,
     * or with status {@code 400 (Bad Request)} if the applicationUser is not valid,
     * or with status {@code 404 (Not Found)} if the applicationUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the applicationUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/application-users/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ApplicationUser> partialUpdateApplicationUser(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ApplicationUser applicationUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update ApplicationUser partially : {}, {}", id, applicationUser);
        if (applicationUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicationUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicationUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApplicationUser> result = applicationUserRepository
            .findById(applicationUser.getId())
            .map(
                existingApplicationUser -> {
                    if (applicationUser.getSoldeUser() != null) {
                        existingApplicationUser.setSoldeUser(applicationUser.getSoldeUser());
                    }

                    return existingApplicationUser;
                }
            )
            .map(applicationUserRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, applicationUser.getId())
        );
    }

    /**
     * {@code GET  /application-users} : get all the applicationUsers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of applicationUsers in body.
     */
    @GetMapping("/application-users")
    public ResponseEntity<List<ApplicationUser>> getAllApplicationUsers(Pageable pageable) {
        log.debug("REST request to get a page of ApplicationUsers");
        Page<ApplicationUser> page = applicationUserRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /application-users/:id} : get the "id" applicationUser.
     *
     * @param id the id of the applicationUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the applicationUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/application-users/{id}")
    public ResponseEntity<ApplicationUser> getApplicationUser(@PathVariable String id) {
        log.debug("REST request to get ApplicationUser : {}", id);
        Optional<ApplicationUser> applicationUser = applicationUserRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(applicationUser);
    }

    /**
     * {@code DELETE  /application-users/:id} : delete the "id" applicationUser.
     *
     * @param id the id of the applicationUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/application-users/{id}")
    public ResponseEntity<Void> deleteApplicationUser(@PathVariable String id) {
        log.debug("REST request to delete ApplicationUser : {}", id);
        applicationUserRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
