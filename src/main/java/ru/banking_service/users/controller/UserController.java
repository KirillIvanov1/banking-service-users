package ru.banking_service.users.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.banking_service.users.model.User;
import ru.banking_service.users.service.UserService;

@RestController
@RequestMapping("api/v1")
public class UserController {
private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestParam(name = "first_name") String firstName,
                                        @RequestParam(name = "last_name") String lastName) {
        User user = userService.createUser(firstName, lastName);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUser(@PathVariable @Valid Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>("No user with such id found", HttpStatus.NOT_FOUND);
    }
}
