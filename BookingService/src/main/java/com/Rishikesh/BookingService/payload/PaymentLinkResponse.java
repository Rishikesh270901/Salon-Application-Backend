package com.Rishikesh.BookingService.payload;

import lombok.Data;

@Data
public class PaymentLinkResponse {

    private String paymentLinkURL;

    private String paymentLinkId;
}
