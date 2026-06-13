package com.Rishikesh.ServiceOfferingService.controller;

import com.Rishikesh.ServiceOfferingService.modal.ServiceOffering;
import com.Rishikesh.ServiceOfferingService.payload.CategoryDTO;
import com.Rishikesh.ServiceOfferingService.payload.SalonDTO;
import com.Rishikesh.ServiceOfferingService.payload.ServiceDTO;
import com.Rishikesh.ServiceOfferingService.service.ServiceOfferingService;
import com.Rishikesh.ServiceOfferingService.service.client.CategoryFeignClient;
import com.Rishikesh.ServiceOfferingService.service.client.SalonFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/service-offering/salon-owner")
public class SalonServiceOfferingController {

    private final ServiceOfferingService serviceOfferingService;
    private final SalonFeignClient salonFeignClient;
    private final CategoryFeignClient categoryFeignClient;


    @PostMapping
    public ResponseEntity<ServiceOffering> createService(@RequestBody ServiceDTO serviceDTO,
                                                         @RequestHeader("Authorization") String jwt
                                                         ) throws Exception {

        SalonDTO salonDTO = salonFeignClient.getSalonByOwnerId(jwt).getBody();

        CategoryDTO categoryDTO = categoryFeignClient.getCategoriesByIdAndSalon(serviceDTO.getCategoryId(), salonDTO.getId()).getBody();

        ServiceOffering serviceOffering = serviceOfferingService.createService(salonDTO, serviceDTO, categoryDTO);
        return ResponseEntity.ok(serviceOffering);
    }

    @PutMapping("/{serviceId}")
    public ResponseEntity<ServiceOffering> updateService(@PathVariable Long serviceId, @RequestBody ServiceOffering serviceOffering){

//        SalonDTO salonDTO = new SalonDTO();
//        salonDTO.setId(1L); // Replace with actual salon ID
//
//        CategoryDTO categoryDTO = new CategoryDTO();
//        categoryDTO.setId(1L);

        ServiceOffering serviceOfferings = serviceOfferingService.updateService(serviceId, serviceOffering);
        return ResponseEntity.ok(serviceOfferings);
    }
}
