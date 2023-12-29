package ru.annakirillova.restaurantvoting.web.user;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.annakirillova.restaurantvoting.model.User;
import ru.annakirillova.restaurantvoting.repository.datajpa.DataJpaUserRepository;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = AdminUserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminUserController {
    static final String REST_URL = "/admin/users";

    @Autowired
    private DataJpaUserRepository repository;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createWithLocation(@Valid @RequestBody User user) {
        log.info("create a user {}", user);
        User created = repository.save(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete the user {}", id);
        repository.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody User user, @PathVariable int id) {
        log.info("update the user {} with id={}", user, id);
        repository.save(user);
    }

    @GetMapping("/{id}")
    public User get(@PathVariable int id) {
        log.info("get the user with id={}", id);
        return repository.get(id);
    }

    @GetMapping
    public List<User> getAll() {
        log.info("get all users");
        return repository.getAll();
    }
}