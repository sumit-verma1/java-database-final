package com.project.code.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.project.code.Model.Customer;
import com.project.code.Model.Review;
import com.project.code.Repository.CustomerRepository;
import com.project.code.Repository.ReviewRepository;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CustomerRepository customerRepository;

    // GET /reviews
    @GetMapping
    public Map<String, Object> getReviews() {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "reviews",
                reviewRepository.findAll());

        return response;
    }

    // GET /reviews/{storeId}/{productId}
    @GetMapping("/{storeId}/{productId}")
    public Map<String, Object> getReviews(
            @PathVariable Long storeId,
            @PathVariable Long productId) {

        Map<String, Object> response =
                new HashMap<>();

        List<Map<String, Object>>
                reviewList =
                new ArrayList<>();

        List<Review> reviews =
                reviewRepository
                .findByStoreIdAndProductId(
                        storeId,
                        productId);

        for (Review review : reviews) {

            Map<String, Object>
                    reviewData =
                    new HashMap<>();

            reviewData.put(
                    "comment",
                    review.getComment());

            reviewData.put(
                    "rating",
                    review.getRating());

            Optional<Customer>
                    customer =
                    customerRepository
                    .findById(
                            review
                            .getCustomerId());

            if (customer.isPresent()) {

                reviewData.put(
                        "customerName",
                        customer.get()
                                .getName());

            } else {

                reviewData.put(
                        "customerName",
                        "Unknown");
            }

            reviewList.add(
                    reviewData);
        }

        response.put(
                "reviews",
                reviewList);

        return response;
    }
}
