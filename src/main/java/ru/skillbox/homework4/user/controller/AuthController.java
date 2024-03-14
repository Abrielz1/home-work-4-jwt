package ru.skillbox.homework4.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.homework4.exception.exceptions.AlreadyExistsException;
import ru.skillbox.homework4.security.SecurityService;
import ru.skillbox.homework4.user.dto.UserDto;
import ru.skillbox.homework4.user.repository.UserRepository;
import ru.skillbox.homework4.web.dto.AuthResponse;
import ru.skillbox.homework4.web.dto.CreateUserRequest;
import ru.skillbox.homework4.web.dto.LoginRequest;
import ru.skillbox.homework4.web.dto.RefreshTokenRequest;
import ru.skillbox.homework4.web.dto.RefreshTokenResponse;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    private final SecurityService securityService;

    @PostMapping("/signing")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse authUser(@RequestBody LoginRequest loginRequest) {

        return securityService.authenticationUser(loginRequest);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public UserDto registerUser(@RequestBody CreateUserRequest request) {

        if (userRepository.existsByUsername(request.getUsername()) &&
                userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("Email: %s or Username: %s already taken! at time "
                    .formatted(request.getUsername(), request.getEmail())
                    + LocalDateTime.now());

        }


        return securityService.register(request);
    }

    @PostMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public RefreshTokenResponse refreshToken(@RequestBody RefreshTokenRequest request) {

        return securityService.refreshToken(request);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public String logoutUser(@AuthenticationPrincipal UserDetails details) {

        securityService.logout();
        return "User was logout! Username is: "
                + details.getUsername()
                + " at time "
                + LocalDateTime.now();
    }
}
