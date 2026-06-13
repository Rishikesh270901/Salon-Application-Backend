package com.Rishikesh.BookingService.service.client;

import com.Rishikesh.BookingService.payload.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("USER-SERVICE")
public interface UserFeignClient {

    @GetMapping("/api/users/{Id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long Id) throws Exception;

    @GetMapping("/api/users/profile")
    public ResponseEntity<UserDTO> getUserProfile(@RequestHeader("Authorization") String jwt) throws Exception;
}
