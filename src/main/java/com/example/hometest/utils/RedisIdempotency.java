package com.example.hometest.utils;

import com.example.hometest.model.dto.payment.IdempotencyCache;
import com.example.hometest.model.dto.payment.PaymentResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisIdempotency {
    private final StringRedisTemplate redisTemplate;
    private final RedissonClient redisson;
    private final ObjectMapper mapper = new ObjectMapper();
    private static final Duration TTL = Duration.ofMinutes(10);


    private String dataKey(String key) {
        return "idempotency:" + key;
    }

    private String lockKey(String key) {
        return "idempotency:lock:" + key;
    }

    public IdempotencyCache getCache(String key) throws Exception {
        String raw = redisTemplate.opsForValue().get(dataKey(key));
        if (raw == null) return null;
        return mapper.readValue(raw, IdempotencyCache.class);
    }

    public void saveCompleted(String key, String requestHash, PaymentResponse response) throws Exception {
        IdempotencyCache cache = new IdempotencyCache(requestHash, response);
        String json = mapper.writeValueAsString(cache);
        redisTemplate.opsForValue().set(dataKey(key), json, TTL);
    }

    public RLock getLock(String key) {
        return redisson.getLock(lockKey(key));
    }
}
