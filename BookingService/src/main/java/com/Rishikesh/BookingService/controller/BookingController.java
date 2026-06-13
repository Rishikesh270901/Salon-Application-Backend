package com.Rishikesh.BookingService.controller;


import com.Rishikesh.BookingService.domain.BookingStatus;
import com.Rishikesh.BookingService.domain.PaymentMethod;
import com.Rishikesh.BookingService.mapper.BookingMapper;
import com.Rishikesh.BookingService.modal.Booking;
import com.Rishikesh.BookingService.modal.SalonReport;
import com.Rishikesh.BookingService.payload.*;
import com.Rishikesh.BookingService.service.BookingService;
import com.Rishikesh.BookingService.service.client.PaymentFeignClient;
import com.Rishikesh.BookingService.service.client.SalonFeignClient;
import com.Rishikesh.BookingService.service.client.ServiceOfferingFeignClient;
import com.Rishikesh.BookingService.service.client.UserFeignClient;
import com.Rishikesh.BookingService.service.impl.BookingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final UserFeignClient userFeignClient;
    private final SalonFeignClient salonFeignClient;
    private final ServiceOfferingFeignClient serviceOfferingFeignClient;
    private final PaymentFeignClient paymentFeignClient;

    @PostMapping
    public ResponseEntity<PaymentLinkResponse> createBooking(@RequestParam Long salonId,
                                                 @RequestParam PaymentMethod paymentMethod,
                                                 @RequestBody BookingRequest bookingRequest,
                                                 @RequestHeader("Authorization") String jwt) throws Exception {

        UserDTO userDTO = userFeignClient.getUserProfile(jwt).getBody();

        SalonDTO salonDTO = salonFeignClient.getSalonById(salonId).getBody();

        Set<ServiceDTO> serviceDTOSet = serviceOfferingFeignClient.getServiceOfferingByIds(bookingRequest.getServiceIds()).getBody();

        if(serviceDTOSet.isEmpty()){
            throw new Exception("Service not found");
        }

        Booking booking = bookingService.createBooking(bookingRequest, userDTO, salonDTO, serviceDTOSet);

        BookingDTO bookingDTO = BookingMapper.toDTO(booking);

        PaymentLinkResponse res = paymentFeignClient.createPaymentLink(bookingDTO, paymentMethod, jwt).getBody();

        return ResponseEntity.ok(res);

    }

    @GetMapping("/customer")
    public ResponseEntity<Set<BookingDTO>> getBookingsByCustomer(@RequestHeader("Authorization") String jwt) throws Exception {

        UserDTO userDTO = userFeignClient.getUserProfile(jwt).getBody();
        if(userDTO==null){
            throw new Exception("User not found from jwt token.");
        }
        List<Booking> bookings = bookingService.getBookingsByCustomer(userDTO.getId());

        return ResponseEntity.ok(getBookingDTOs(bookings));
    }

    @GetMapping("/salon")
    public ResponseEntity<Set<BookingDTO>> getBookingsBySalon(@RequestHeader("Authorization") String jwt) throws Exception {

        SalonDTO salonDTO = salonFeignClient.getSalonByOwnerId(jwt).getBody();
        List<Booking> bookings = bookingService.getBookingsBySalon(salonDTO.getId());

        return ResponseEntity.ok(getBookingDTOs(bookings));
    }


    private Set<BookingDTO> getBookingDTOs(List<Booking> bookings) {
        return bookings.stream().map(BookingMapper::toDTO).collect(Collectors.toSet());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBookingsById(@PathVariable Long id) throws Exception {

        Booking bookings = bookingService.getBookingById(id);

        return ResponseEntity.ok(BookingMapper.toDTO(bookings));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<BookingDTO> updateBookingStatus(@PathVariable Long id,
                                                               @RequestParam BookingStatus status) throws Exception {

        Booking bookings = bookingService.updateBooking(id, status);

        return ResponseEntity.ok(BookingMapper.toDTO(bookings));
    }

    @GetMapping("/slots/salon/{salonId}/date/{date}")
    public ResponseEntity<List<BookingSlotDTO>> getBookedSlot(@RequestParam(required = false) LocalDate date, @PathVariable Long salonId) throws Exception {

        List<Booking> bookings = bookingService.getBookingsByDate(date, salonId);

        List<BookingSlotDTO> slotsDTOs = bookings.stream().map(booking -> {
            BookingSlotDTO slotDTO = new BookingSlotDTO();
            slotDTO.setStartTime(booking.getStartTime());
            slotDTO.setEndTime(booking.getEndTime());
            return slotDTO;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(slotsDTOs);
    }

    @GetMapping("/report")
    public ResponseEntity<SalonReport> getSalonReport(@RequestHeader("Authorization") String jwt) throws Exception {

        SalonDTO salonDTO = salonFeignClient.getSalonByOwnerId(jwt).getBody();
       SalonReport report = bookingService.getSalonreport(salonDTO.getId());

        return ResponseEntity.ok(report);
    }
}
