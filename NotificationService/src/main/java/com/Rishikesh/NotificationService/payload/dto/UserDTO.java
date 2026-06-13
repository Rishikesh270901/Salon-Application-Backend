package com.Rishikesh.NotificationService.payload.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserDTO {

    private Long Id;

    private String fullname;

    private String email;
}
