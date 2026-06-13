package com.Rishikesh.NotificationService.mapper;

import com.Rishikesh.NotificationService.modal.Notification;
import com.Rishikesh.NotificationService.payload.dto.BookingDTO;
import com.Rishikesh.NotificationService.payload.dto.NotificationDTO;

public class NotificationMapper {

    public static NotificationDTO toDTO(Notification notification, BookingDTO bookingDTO){

        NotificationDTO notificationDTO = new NotificationDTO();

        notificationDTO.setId(notification.getId());
        notificationDTO.setType(notification.getType());
        notificationDTO.setIsRead(notification.getIsRead());
        notificationDTO.setDescription(notification.getDescription());
        notificationDTO.setBookingId(notification.getBookingId());
        notificationDTO.setUserId(notification.getUserId());
        notificationDTO.setSalonId(notification.getSalonId());
        notificationDTO.setCreatedAt(notification.getCreatedAt());
        notificationDTO.setBooking(bookingDTO);
        return notificationDTO;
    }

}
