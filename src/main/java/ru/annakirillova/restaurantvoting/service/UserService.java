package ru.annakirillova.restaurantvoting.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.annakirillova.restaurantvoting.model.User;
import ru.annakirillova.restaurantvoting.repository.UserRepository;
import ru.annakirillova.restaurantvoting.to.UserTo;
import ru.annakirillova.restaurantvoting.util.UsersUtil;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");

    private final UserRepository userRepository;

    public User get(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public void delete(int id) {
        userRepository.delete(id);
    }

    public List<User> getAll() {
        return userRepository.findAll(SORT_NAME_EMAIL);
    }

    public void updatePartially(UserTo userTo, User user) {
        User updatedUser = UsersUtil.updateFromTo(user, userTo);
        userRepository.prepareAndSave(updatedUser);
    }

    public void updateFull(User user, int id) {
        user.setId(id);
        userRepository.prepareAndSave(user);
    }

    public User create(User user) {
        return userRepository.prepareAndSave(user);
    }

    public Optional<User> findByEmailIgnoreCase(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }
}
