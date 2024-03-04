package ru.skillbox.homework4.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.homework4.exception.exceptions.RefreshTokenException;
import ru.skillbox.homework4.security.jwt.JwtUtils;
import ru.skillbox.homework4.user.dto.UserDto;
import ru.skillbox.homework4.user.model.RefreshToken;
import ru.skillbox.homework4.user.model.User;
import ru.skillbox.homework4.user.repository.UserRepository;
import ru.skillbox.homework4.user.service.RefreshTokenService;
import ru.skillbox.homework4.web.dto.AuthResponse;
import ru.skillbox.homework4.web.dto.*;
import java.util.List;
import static ru.skillbox.homework4.user.mapper.UserMapper.USER_MAPPER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SecurityService {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final RefreshTokenService refreshTokenService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public AuthResponse authenticationUser(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        RefreshToken refreshToken = refreshTokenService.create(userDetails.getId());

        return AuthResponse.builder()
                .id(userDetails.getId())
                .token(jwtUtils.generateJwtToken(userDetails))
                .refreshToken(refreshToken.getToken())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .roles(roles)
                .build();
    }

    @Transactional
    public UserDto register(CreateUserRequest createUserRequest) {

        User user = User.builder()
                .username(createUserRequest.getUsername())
                .email(createUserRequest.getEmail())
                .password(passwordEncoder.encode(createUserRequest.getPassword()))
                .build();

        user.setRoles(createUserRequest.getRoles());
        userRepository.save(user);
        return USER_MAPPER.toUserDto(user);
    }

    @Transactional
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {

        String requestTokenRefresh = request.getRefreshToken();
        return refreshTokenService.getByRefreshToken(requestTokenRefresh)
                .map(refreshTokenService::checkRefreshToken)
                .map(RefreshToken::getId)
                .map(userId -> {
                    User user = userRepository.findById(userId).orElseThrow(() ->
                            new RefreshTokenException("Exception for userId: " + userId));

                    String token = jwtUtils.generateTokenFromUserName(user.getUsername());
                    return new RefreshTokenResponse(token, refreshTokenService.create(userId).getToken());
                }).orElseThrow(() -> new RefreshTokenException("RefreshToken is not found!"));
    }

    @Transactional
    public void logout() {

        var currentPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentPrincipal instanceof AppUserDetails userDetails) {
          refreshTokenService.deleteByUserId(userDetails.getId());
        }
    }
}
