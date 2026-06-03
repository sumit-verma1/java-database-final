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

    // Get Reviews By Store Id & Product Id
    @GetMapping("/{storeId}/{productId}")
    public Map<String, Object> getReviews(
            @PathVariable Long storeId,
            @PathVariable Long productId) {

        Map<String, Object> response =
                new HashMap<>();

        List<Map<String, Object>> reviewList =
                new ArrayList<>();

        // Fetch Reviews
        List<Review> reviews =
                reviewRepository
                .findByStoreIdAndProductId(
                        storeId,
                        productId);

        // Filter Required Data
        for (Review review : reviews) {

            Map<String, Object> reviewData =
                    new HashMap<>();

            // Add Comment
            reviewData.put(
                    "comment",
                    review.getComment());

            // Add Rating
            reviewData.put(
                    "rating",
                    review.getRating());

            // Get Customer Name
            Optional<Customer> customer =
                    customerRepository
                    .findById(
                            review.getCustomerId());

            reviewData.put(
                    "customerName",
                    customer.isPresent()
                            ? customer.get()
                                .getName()
                            : "Unknown");

            reviewList.add(reviewData);
        }

        response.put(
                "reviews",
                reviewList);

        return response;
    }
}
