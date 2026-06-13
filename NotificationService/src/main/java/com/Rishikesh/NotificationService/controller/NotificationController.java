package com.Rishikesh.NotificationService.controller;

import com.Rishikesh.NotificationService.mapper.NotificationMapper;
import com.Rishikesh.NotificationService.modal.Notification;
import com.Rishikesh.NotificationService.payload.dto.BookingDTO;
import com.Rishikesh.NotificationService.payload.dto.NotificationDTO;
import com.Rishikesh.NotificationService.service.NotificationService;
import com.Rishikesh.NotificationService.service.client.BookingFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final BookingFeignClient bookingFeignClient;

    @PostMapping
    public ResponseEntity<NotificationDTO> createNotification(@RequestBody Notification notification) throws Exception {
        return ResponseEntity.ok(notificationService.createNotification(notification));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDTO>> getNotificationByUserId(@PathVariable Long userId){
        List<Notification> notifications = notificationService.getAllNotificationByUserId(userId);

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

    @PutMapping("/notificationId")
    public ResponseEntity<NotificationDTO> markNotificationAsRead(@PathVariable Long notificationId) throws Exception {

        Notification notification = notificationService.markNotificationAsRead(notificationId);
        BookingDTO bookingDTO = bookingFeignClient.getBookingsById(notification.getBookingId()).getBody();
        return ResponseEntity.ok(NotificationMapper.toDTO(notification, bookingDTO));
    }

}
