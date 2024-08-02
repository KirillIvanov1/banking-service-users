package ru.banking_service.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.banking_service.users.model.Role;
import ru.banking_service.users.model.User;
import ru.banking_service.users.util.authentication.JwtAuthenticationResponse;
import ru.banking_service.users.util.authentication.SignInRequest;
import ru.banking_service.users.util.authentication.SignUpRequest;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    //регистрация пользователя

    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        var user = User.builder()
                .passportNumber(request.getPassportNumber())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userService.create(user);

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getPassportNumber(), request.getPassword()
        ));
        var user = userService.userDetailsService().loadUserByUsername(request.getPassportNumber());
        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }


}
