package com.Rishikesh.ReviewService.service.impl;

import com.Rishikesh.ReviewService.dto.ReviewRequest;
import com.Rishikesh.ReviewService.dto.SalonDTO;
import com.Rishikesh.ReviewService.dto.UserDTO;
import com.Rishikesh.ReviewService.modal.Review;
import com.Rishikesh.ReviewService.repository.ReviewRepository;
import com.Rishikesh.ReviewService.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public Review createReview(ReviewRequest reviewRequest, UserDTO userDTO, SalonDTO salonDTO) {

        Review review = new Review();
        review.setReviewText(reviewRequest.getReviewText());
        review.setRating(reviewRequest.getRating());
        review.setUserId(userDTO.getId());
        review.setSalonId(salonDTO.getId());

        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewsBySalonId(Long salonId) {
        return reviewRepository.findBySalonId(salonId);
    }

    private Review getReviewById(Long id) throws Exception {
        return reviewRepository.findById(id).orElseThrow(()->new Exception("Review not exist"));
    }

    @Override
    public Review updateReview(ReviewRequest reviewRequest, Long reviewId, Long userId) throws Exception {

        Review review = getReviewById(reviewId);
        if(!review.getUserId().equals(userId)){
            throw new Exception("You don't have permission to update this review");
        }

        review.setReviewText(reviewRequest.getReviewText());
        review.setRating(reviewRequest.getRating());

        return reviewRepository.save(review);
    }

    @Override
    public void delteReview(Long reviewId, Long userId) throws Exception {
        Review review = getReviewById(reviewId);
        if(!review.getUserId().equals(userId)){
            throw new Exception("You don't have permission to delete this review");
        }

        reviewRepository.delete(review);

    }
}
