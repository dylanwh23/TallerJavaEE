package com.tallerjava.tallerjava.Compra.interfase;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;

import java.time.Duration;

public class RateLimiter {
    private static final Bucket bucket = Bucket.builder()
            .addLimit(Bandwidth.classic(5, Refill.intervally(5, Duration.ofSeconds(1))))
            .build();

    public static boolean tryConsume() {
        return bucket.tryConsume(1);
    }
}
