package com.Rishikesh.SalonService.controller;

import com.Rishikesh.SalonService.mapper.SalonMapper;
import com.Rishikesh.SalonService.modal.Salon;
import com.Rishikesh.SalonService.payload.SalonDTO;
import com.Rishikesh.SalonService.payload.UserDTO;
import com.Rishikesh.SalonService.service.SalonService;
import com.Rishikesh.SalonService.service.client.UserfeignClient;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/salons")
public class SalonController {

    private final SalonService salonService;
    private final UserfeignClient userfeignClient;

    @PostMapping
    public ResponseEntity<SalonDTO> createSalon(@RequestBody SalonDTO salonDTO,
                                                @RequestHeader("Authorization") String jwt) throws Exception {

        UserDTO user = userfeignClient.getUserProfile(jwt).getBody();
        Salon newSalon = salonService.createSalon(salonDTO, user);
        SalonDTO responseSalonDTO = SalonMapper.mapToDTO(newSalon);
        return ResponseEntity.ok(responseSalonDTO);
    }

    @PatchMapping("/{salonId}")
    public ResponseEntity<SalonDTO> updateSalon(@RequestBody SalonDTO salonDTO,
                                                @PathVariable Long salonId,
                                                @RequestHeader("Authorization") String jwt) throws Exception {
        UserDTO user = userfeignClient.getUserProfile(jwt).getBody();
        Salon updateSalon = salonService.updateSalon(salonDTO, user, salonId);
        SalonDTO responseSalonDTO = SalonMapper.mapToDTO(updateSalon);
        return ResponseEntity.ok(responseSalonDTO);
    }

    @GetMapping
    public ResponseEntity<List<SalonDTO>> getSalons(){
        List<Salon> salons = salonService.getAllSalons();
        List<SalonDTO> salonDTOs = salons.stream().map(SalonMapper::mapToDTO
                ).toList();
        return ResponseEntity.ok(salonDTOs);
    }

    @GetMapping("/{salonId}")
    public ResponseEntity<SalonDTO> getSalonById(@PathVariable Long salonId) throws Exception {
        Salon salon = salonService.getSalonById(salonId);
        SalonDTO responseSalonDTO = SalonMapper.mapToDTO(salon);
        return ResponseEntity.ok(responseSalonDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SalonDTO>> searchSalon(@RequestParam("city") String city) throws Exception {
        List<Salon> salons = salonService.searchSalonByCityName(city);
        List<SalonDTO> salonDTOs = salons.stream().map(SalonMapper::mapToDTO
        ).toList();
        return ResponseEntity.ok(salonDTOs);
    }

    @GetMapping("/owner")
    public ResponseEntity<SalonDTO> getSalonByOwnerId(@RequestHeader("Authorization") String jwt) throws Exception {
        UserDTO user = userfeignClient.getUserProfile(jwt).getBody();
        if(user == null){
            throw new Exception("User not found from Jwt");
        }
        Salon salon = salonService.getSalonByOwnerId(user.getId());
        SalonDTO responseSalonDTO = SalonMapper.mapToDTO(salon);
        return ResponseEntity.ok(responseSalonDTO);
    }


}
