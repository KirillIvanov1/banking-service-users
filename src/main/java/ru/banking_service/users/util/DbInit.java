package ru.banking_service.users.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.banking_service.users.model.Role;
import ru.banking_service.users.model.User;
import ru.banking_service.users.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Component
public class DbInit {
    private final UserRepository userRepository;

    @Autowired
    public DbInit(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    private void initializeUsers() {
        List<User> users = userRepository.findAll();
        users.stream()
                .filter(user -> user.getRole() == null)
                .forEach(u -> u.setRole(Role.USER));
        userRepository.saveAll(users);
    }
}
