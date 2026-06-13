package com.Rishikesh.NotificationService.service.client;

import com.Rishikesh.NotificationService.payload.dto.BookingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("BOOKING-SERVICE")
public interface BookingFeignClient {

    @GetMapping("/api/bookings/{id}")
    public ResponseEntity<BookingDTO> getBookingsById(@PathVariable Long id) throws Exception;

}
