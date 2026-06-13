package com.Rishikesh.CategoryService.controller;


import com.Rishikesh.CategoryService.modal.Category;
import com.Rishikesh.CategoryService.payload.SalonDTO;
import com.Rishikesh.CategoryService.service.CategoryService;
import com.Rishikesh.CategoryService.service.client.SalonFeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/categories/salon-owner")
public class SalonCategoryController {

    private final CategoryService categoryService;
    private final SalonFeignClient salonFeignClient;

    public SalonCategoryController(CategoryService categoryService, SalonFeignClient salonFeignClient) {
        this.categoryService = categoryService;
        this.salonFeignClient = salonFeignClient;
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category,
                                                   @RequestHeader("Authorization") String jwt) throws Exception {
        SalonDTO salonDTO = salonFeignClient.getSalonByOwnerId(jwt).getBody();
        Category createCategory = categoryService.createCategory(category, salonDTO);
        return ResponseEntity.ok(createCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id,
                                                 @RequestHeader("Authorization") String jwt) throws Exception {
        SalonDTO salonDTO = salonFeignClient.getSalonByOwnerId(jwt).getBody();
        categoryService.deleteCategoryById(id, salonDTO.getId());
        return ResponseEntity.ok("Category Deleted Successfully.");
    }

    @GetMapping("/salon/{salonId}/category/{id}")
    public ResponseEntity<Category> getCategoriesByIdAndSalon(@PathVariable Long id,
                                                                   @PathVariable Long salonId) throws Exception {

        Category categories = categoryService.findByIdAndSalonId(id, salonId);
        return ResponseEntity.ok(categories);
    }

}

