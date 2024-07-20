package com.buildingweb.service.impl;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.buildingweb.service.BlackListService;
import com.buildingweb.service.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BlackListServiceImpl implements BlackListService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtService jwtService;
    private final String BLACK_LIST_PREFIX = "Blacklist ";

    @Override
    public void addToBlackList(String token) {
        Date expirationTime = jwtService.extractExpirationToken(token);
        redisTemplate.opsForValue().set(BLACK_LIST_PREFIX + token, true,
                expirationTime.getTime() - System.currentTimeMillis(),
                TimeUnit.MILLISECONDS);
    }

    @Override
    public Boolean isBlackList(String token) {
        return (Boolean) redisTemplate.opsForValue().get(BLACK_LIST_PREFIX + token);
    }
}