package ru.skillbox.homework4.user.repository;

import org.springframework.data.repository.CrudRepository;
import ru.skillbox.homework4.user.model.RefreshToken;
import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUserId(Long UserId);
}
