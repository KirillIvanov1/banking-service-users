package ru.banking_service.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.banking_service.users.model.User;
public interface UserRepository extends JpaRepository<User, Long> {
}
