package com.maveric.accountservice.feignclient;

import com.maveric.accountservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(value = "user", url = "http://localhost:3005/api/v1")
public interface FeignConsumer {

    @GetMapping("/users/{userId}")
    ResponseEntity<UserDto> getUserById(@PathVariable String userId);
}