package com.project.code.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Map<String, String> addStore(
            @RequestBody Store store) {

        Map<String, String> response =
                new HashMap<>();

        Store savedStore =
                storeRepository.save(store);

        response.put(
                "message",
                "Store successfully created with ID: "
                        + savedStore.getId());

        return response;
    }

    // Validate Store
    @GetMapping("validate/store/{id}")
    public boolean validateStore(
            @PathVariable Long id) {

        return storeRepository
                .existsById(id);
    }

    // Place Order
    @PostMapping("/placeOrder")
    public Map<String, String> placeOrder(
            @RequestBody
            PlaceOrderRequestDTO requestDTO) {

        Map<String, String> response =
                new HashMap<>();

        try {

            orderService
                    .saveOrder(requestDTO);

            response.put(
                    "message",
                    "Order placed successfully");

        } catch (Exception e) {

            response.put(
                    "Error",
                    e.getMessage());
        }

        return response;
    }
}
