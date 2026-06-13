package com.Rishikesh.BookingService.service.client;

import com.Rishikesh.BookingService.domain.PaymentMethod;
import com.Rishikesh.BookingService.payload.BookingDTO;
import com.Rishikesh.BookingService.payload.PaymentLinkResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("PAYMENT-SERVICE")
public interface PaymentFeignClient {

    @PostMapping("/api/payments/create")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(@RequestBody BookingDTO booking,
                                                                 @RequestParam PaymentMethod paymentMethod,
                                                                 @RequestHeader("Authorization") String jwt);

}
