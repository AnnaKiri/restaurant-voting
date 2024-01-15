package ru.annakirillova.restaurantvoting.service;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.annakirillova.restaurantvoting.error.NotFoundException;
import ru.annakirillova.restaurantvoting.model.User;
import ru.annakirillova.restaurantvoting.repository.UserRepository;
import ru.annakirillova.restaurantvoting.to.UserTo;
import ru.annakirillova.restaurantvoting.util.UsersUtil;

import java.util.List;
import java.util.Optional;

import static ru.annakirillova.restaurantvoting.validation.ValidationUtil.assureIdConsistent;
import static ru.annakirillova.restaurantvoting.validation.ValidationUtil.checkNew;

@Service
@AllArgsConstructor
public class UserService {
    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");

    private final UserRepository userRepository;

    public void delete(int id) {
        userRepository.deleteExisted(id);
    }

    public List<User> getAll() {
        return userRepository.findAll(SORT_NAME_EMAIL);
    }

    public void updatePartially(UserTo userTo, User user) {
        User updatedUser = UsersUtil.updateFromTo(user, userTo);
        userRepository.prepareAndSave(updatedUser);
    }

    public void updateFull(User user, int id) {
        assureIdConsistent(user, id);
        userRepository.prepareAndSave(user);
    }

    public User create(User user) {
        checkNew(user);
        return userRepository.prepareAndSave(user);
    }

    public Optional<User> findByEmailIgnoreCase(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    public User getExistedByEmail(String email) {
        return findByEmailIgnoreCase(email).orElseThrow(() -> new NotFoundException("User with email=" + email + " not found"));
    }

    @Transactional
    public void setEnabled(int userId, boolean enabled) {
        User user = userRepository.getExisted(userId);
        user.setEnabled(enabled);
    }
}
