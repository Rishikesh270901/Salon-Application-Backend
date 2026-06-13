package com.Rishikesh.ReviewService.service;

import com.Rishikesh.ReviewService.dto.ReviewRequest;
import com.Rishikesh.ReviewService.dto.SalonDTO;
import com.Rishikesh.ReviewService.dto.UserDTO;
import com.Rishikesh.ReviewService.modal.Review;

import java.util.List;

public interface ReviewService {

    Review createReview(ReviewRequest reviewRequest, UserDTO userDTO, SalonDTO salonDTO);

    List<Review> getReviewsBySalonId(Long salonId);

    Review updateReview(ReviewRequest reviewRequest, Long reviewId, Long userId) throws Exception;

    void delteReview(Long reviewId, Long userId) throws Exception;

}
