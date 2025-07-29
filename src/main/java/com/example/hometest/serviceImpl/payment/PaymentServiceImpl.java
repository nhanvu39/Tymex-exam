package com.example.hometest.serviceImpl.payment;

import com.example.hometest.model.dto.payment.IdempotencyCache;
import com.example.hometest.model.dto.payment.PaymentRequest;
import com.example.hometest.model.dto.payment.PaymentResponse;
import com.example.hometest.service.payment.PaymentService;
import com.example.hometest.utils.RedisIdempotency;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private RedisIdempotency redisIdempotency;


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResponseEntity<?> processPayment(String idempotencyKey, PaymentRequest request) throws Exception {
        String requestHash = String.valueOf(request.hashCode()); // get hash request
        IdempotencyCache existingCache = redisIdempotency.getCache(idempotencyKey);
        if (existingCache != null) {
            if (!existingCache.getRequestHash().equals(requestHash)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Idempotency key reused with a different request");
            }
            return ResponseEntity.ok(existingCache.getResponse());
        }

        // 2. Acquire lock
        RLock lock = redisIdempotency.getLock(idempotencyKey);
        if (!lock.tryLock(0, TimeUnit.SECONDS)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body("Processing in progress for this key");
        }
        try {
            // Double-check after acquiring the lock
            existingCache = redisIdempotency.getCache(idempotencyKey);
            if (existingCache != null) {
                if (!existingCache.getRequestHash().equals(requestHash)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("Idempotency key reused with a different request");
                }
                return ResponseEntity.ok(existingCache.getResponse());
            }

            // 3. Process payment
            PaymentResponse response = performPayment(request);

            // 4. Save result
            redisIdempotency.saveCompleted(idempotencyKey, requestHash, response);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
    private PaymentResponse performPayment(PaymentRequest req) {
//        process payment here(ex: call api to stripe ...)
        try {
            // Simulate external payment service taking 10 seconds
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        PaymentResponse resp = new PaymentResponse();
        resp.setTransactionId(UUID.randomUUID().toString());
        resp.setStatus("SUCCESS");
        return resp;
    }

}
