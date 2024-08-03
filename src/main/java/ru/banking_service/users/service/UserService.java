package ru.banking_service.users.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.banking_service.users.model.Role;
import ru.banking_service.users.model.User;
import ru.banking_service.users.repository.UserRepository;
import ru.banking_service.users.util.exceptions.ApplicationException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User createUser(String firstName, String lastName) {
        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build();
        userRepository.save(user);
        return user;
    }

    public boolean userExists(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.isPresent();
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }


    public User getUserByPassportNumber(String passportNumber) {
        log.info("getting user by passport: {}", passportNumber);
        Optional<User> user = userRepository.findByPassportNumber(passportNumber);
        return user.orElse(null);
    }
    @Transactional
    @Cacheable(value="users", key="#userId")
    public User getUserById(Long userId) {
        log.info("getting user by id: {}", userId);
        Optional<User> user = userRepository.findById(userId);
        return user.orElseThrow(() -> new ApplicationException("user-not-found",
                String.format("User with id:%s was not found",userId),
                HttpStatus.NOT_FOUND));
    }

    public UserDetailsService userDetailsService() {
        return this::getUserByPassportNumber;
    }

    public User getCurrentUser() {
        //получение имени пользователя из контекста Spring Security
        var passportNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUserByPassportNumber(passportNumber);
    }

    public void create(User user) {
        userRepository.save(user);
    }

    /**
     * Выдача прав текущему пользователю
     * <p>Нужен для демонстрации</p>
     */
    @Deprecated
    public void getAdmin() {
        User user = getCurrentUser();
        user.setRole(Role.ADMIN);
    }

    @CachePut(value="users", key="#userId")
    public void updateUserFirstAndLastName(Long userId, String firstName, String lastName) {
        User user = getUserById(userId);
        User updatedUser = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .role(user.getRole())
                .email(user.getEmail())
                .id(user.getId())
                .password(user.getPassword())
                .passportNumber(user.getPassportNumber())
                .build();
        userRepository.save(updatedUser);
    }

    @CacheEvict(value="users", key = "#userId")
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
