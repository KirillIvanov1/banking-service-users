package ru.banking_service.users.util.authentication;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.AbstractPasswordEncoder;

public class PasswordEncoder extends AbstractPasswordEncoder {


    @Override
    protected byte[] encode(CharSequence rawPassword, byte[] salt) {
        return new byte[0];
    }
}
