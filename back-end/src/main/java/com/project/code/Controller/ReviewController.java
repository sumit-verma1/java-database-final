package com.project.code.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // Get All Reviews
    @GetMapping
    public Map<String, Object> getAllReviews() {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "reviews",
                reviewRepository.findAll()
        );

        return response;
    }

    // Get Reviews By StoreId & ProductId
    @GetMapping("/{storeId}/{productId}")
    public Map<String, Object> getReviews(
            @PathVariable Long storeId,
            @PathVariable Long productId) {

        Map<String, Object> response =
                new HashMap<>();

        List<Map<String, Object>>
                reviewResponse =
                new ArrayList<>();

        // Fetch Reviews
        List<Review> reviews =
                reviewRepository
                .findByStoreIdAndProductId(
                        storeId,
                        productId);

        // Add Customer Name
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
                            review.getCustomerId());

            reviewData.put(
                    "customerName",
                    customer.isPresent()
                            ? customer.get()
                                    .getName()
                            : "Unknown");

            reviewResponse.add(
                    reviewData);
        }

        response.put(
                "reviews",
                reviewResponse);

        return response;
    }
}
