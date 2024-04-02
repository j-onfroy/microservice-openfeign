package com.company.appnotificationservice.client;

import com.company.appnotificationservice.dto.UserDto;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface AuthClient {
    @GetExchange("/api/v1/auth/user/me")
    UserDto getUserMeByToken(@RequestHeader("Authorization") String token);
}
