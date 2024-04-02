package com.company.appnotificationservice.service;

import com.company.appnotificationservice.client.AuthClient;
import com.company.appnotificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheService {
    private final AuthClient authClient;

    @Cacheable(value = "users", key = "#token")
    public UserDto getUserDtoI(String token) {
        return authClient.getUserMeByToken(token);
    }
}
