package ru.skillbox.homework4.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.skillbox.homework4.exception.exceptions.RefreshTokenException;
import ru.skillbox.homework4.user.model.RefreshToken;
import ru.skillbox.homework4.user.repository.RefreshTokenRepository;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${app.jwt.refreshTokenExpiration}")
    private Duration refreshTokenExpiration;

    private final RefreshTokenRepository refreshTokenRepository;

    public Optional<RefreshToken> getByRefreshToken(String refreshToken) {

        return refreshTokenRepository.findByToken(refreshToken);
    }

    public RefreshToken create(Long userId) {

      var refreshToken = RefreshToken
              .builder()
              .userId(userId)
              .expiryDate(Instant.now().plusMillis(refreshTokenExpiration.toMillis()))
              .token(UUID.randomUUID().toString())
              .build();
       return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken checkRefreshToken(RefreshToken refreshToken) {

        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenException("Refresh token is expired! " + refreshToken.getToken()
            + "Try reLogin!");
        } else {
            return refreshToken;
        }
    }

    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
