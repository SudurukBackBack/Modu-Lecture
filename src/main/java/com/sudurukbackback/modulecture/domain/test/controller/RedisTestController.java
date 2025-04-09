package com.sudurukbackback.modulecture.domain.test.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/test/redis")
@RequiredArgsConstructor
@RestController
public class RedisTestController {

    private final StringRedisTemplate redisTemplate;

    @PostMapping("/set")
    public ResponseEntity<?> setValue(@RequestParam String key, @RequestParam String value) {
        redisTemplate.opsForValue().set(key, value);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get")
    public ResponseEntity<String> getValue(@RequestParam String key) {
        String value = redisTemplate.opsForValue().get(key);
        return ResponseEntity.ok(value);
    }
}
