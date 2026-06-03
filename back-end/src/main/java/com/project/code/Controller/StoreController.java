package com.project.code.Controller;



import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.code.DTO.PlaceOrderRequestDTO;
import com.project.code.Model.Store;
import com.project.code.Repository.StoreRepository;
import com.project.code.Service.OrderService;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderService orderService;

    // Add Store
    @PostMapping
    public Map<String, String> addStore(@RequestBody Store store) {

        Store savedStore = storeRepository.save(store);

        Map<String, String> response = new HashMap<>();
        response.put(
            "message",
            "Store successfully created with ID: " + savedStore.getId()
        );

        return response;
    }

    // Validate Store
    @GetMapping("/validate/{storeId}")
    public boolean validateStore(@PathVariable Long storeId) {

        return storeRepository.existsById(storeId);
    }

    // Place Order
    @PostMapping("/placeOrder")
    public Map<String, String> placeOrder(
            @RequestBody PlaceOrderRequestDTO requestDTO) {

        Map<String, String> response = new HashMap<>();

        try {
            orderService.placeOrder(requestDTO);

            response.put("message", "Order placed successfully");

        } catch (Exception e) {

            response.put("Error", e.getMessage());
        }

        return response;
    }
}
}
