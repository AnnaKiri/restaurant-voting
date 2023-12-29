package ru.annakirillova.restaurantvoting.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.model.User;
import ru.annakirillova.restaurantvoting.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class DataJpaUserRepository {
    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");

    private final UserRepository crudRepository;

    public DataJpaUserRepository(UserRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    public User save(User user) {
        return crudRepository.save(user);
    }

    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }

    public User getByEmail(String email) {
        return crudRepository.getByEmail(email);
    }

    public List<User> getAll() {
        return crudRepository.findAll(SORT_NAME_EMAIL);
    }

    public User get(int id) {
        return crudRepository.findById(id).orElse(null);
    }

    public Optional<User> findByEmailIgnoreCase(String email) {
        return crudRepository.findByEmailIgnoreCase(email);
    }
}