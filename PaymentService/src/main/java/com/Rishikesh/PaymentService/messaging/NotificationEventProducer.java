package com.Rishikesh.PaymentService.messaging;

import com.Rishikesh.PaymentService.payload.response.dto.NotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sentNotification(Long bookingId, Long userId, Long salonId){
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setBookingId(bookingId);
        notificationDTO.setSalonId(salonId);
        notificationDTO.setUserId(userId);
        notificationDTO.setDescription("new booking got confirmed");
        notificationDTO.setType("BOOKING");

        rabbitTemplate.convertAndSend("notification-queue", notificationDTO);

    }

}
