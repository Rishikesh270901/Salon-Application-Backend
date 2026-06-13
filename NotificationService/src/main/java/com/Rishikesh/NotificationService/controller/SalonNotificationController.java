package com.Rishikesh.NotificationService.controller;

import com.Rishikesh.NotificationService.mapper.NotificationMapper;
import com.Rishikesh.NotificationService.modal.Notification;
import com.Rishikesh.NotificationService.payload.dto.BookingDTO;
import com.Rishikesh.NotificationService.payload.dto.NotificationDTO;
import com.Rishikesh.NotificationService.service.NotificationService;
import com.Rishikesh.NotificationService.service.client.BookingFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications/salon-owner")
public class SalonNotificationController {
    private final NotificationService notificationService;
    private final BookingFeignClient bookingFeignClient;

    public SalonNotificationController(NotificationService notificationService, BookingFeignClient bookingFeignClient) {
        this.notificationService = notificationService;
        this.bookingFeignClient = bookingFeignClient;
    }

    @GetMapping("/salon/{salonId}")
    public ResponseEntity<List<NotificationDTO>> getNotificationBySalonId(@PathVariable Long salonId){
        List<Notification> notifications = notificationService.getAllNotificationBySalonId(salonId);

        List<NotificationDTO> notificationDTOS = notifications.stream().map(notification -> {
            BookingDTO bookingDTO = null;
            try {
                bookingDTO = bookingFeignClient.getBookingsById(notification.getBookingId()).getBody();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return NotificationMapper.toDTO(notification, bookingDTO);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(notificationDTOS);
    }

}
