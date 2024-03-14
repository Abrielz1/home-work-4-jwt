package ru.skillbox.homework4.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.stereotype.Component;
import ru.skillbox.homework4.user.model.RefreshToken;

@Slf4j
@Component
public class RedisExpirationEvent {

    @EventListener
    public void handleRedisKeyExpiredEvent(RedisKeyExpiredEvent<RefreshToken> event) {
        RefreshToken expiredRefreshToken = (RefreshToken) event.getValue();

        if (expiredRefreshToken == null) {
            throw new RuntimeException("Refresh token is null in handleRedisKeyExpiredEvent function");
        }

        log.info("Refresh token with key: {}" +
                " has expired and Refresh Token is: {}",
                expiredRefreshToken.getId(), expiredRefreshToken.getToken());
    }
}
