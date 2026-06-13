package com.Rishikesh.ReviewService.repository;

import com.Rishikesh.ReviewService.modal.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findBySalonId(Long salonId);

}
