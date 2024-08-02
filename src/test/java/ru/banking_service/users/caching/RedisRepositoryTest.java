package ru.banking_service.users.caching;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.banking_service.users.AbstractDataJpaTest;
import ru.banking_service.users.AbstractTest;
import ru.banking_service.users.model.User;
import ru.banking_service.users.repository.UserRepository;
import ru.banking_service.users.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Slf4j
@Import({UserRepository.class, UserService.class})
@EnableCaching
public class RedisRepositoryTest extends AbstractDataJpaTest {

    private static final String PASSPORT = "4018123450";
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void givenRedisCaching_whenFindUserById_thenUserReturnedFromCache() {
        User user = User.builder()
                .firstName("Ricardo")
                .lastName("Milos")
                .passportNumber(PASSPORT)
                .build();

        userRepository.save(user);

        User userCacheMiss = userService.getUserByPassportNumber(PASSPORT);
        User userCacheHit = userService.getUserByPassportNumber(PASSPORT);

        assertThat(userCacheMiss).isEqualTo(user);
        assertThat(userCacheHit).isEqualTo(user);

        verify(userRepository, times(1)).findByPassportNumber(PASSPORT);


    }

    //@Test
    @WithMockUser(username = "admin", authorities = {"ADMIN", "USER"})
    public void get() {
        User user1 = userService.createUser("John", "Wick");
        User user2 = userService.createUser("Anthony", "Fantano");

        getAndPrint(user1.getId());
        getAndPrint(user2.getId());
        getAndPrint(user1.getId());
        getAndPrint(user2.getId());
    }

    private void getAndPrint(Long id) {
        log.info("user found: {}", userService.getUserById(id));
    }

}
