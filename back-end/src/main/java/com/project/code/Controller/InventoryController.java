package com.project.code.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.code.Model.CombinedRequest;
import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Repository.InventoryRepository;
import com.project.code.Repository.ProductRepository;
import com.project.code.Service.ServiceClass;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ServiceClass serviceClass;

    // Update Inventory
    @PutMapping
    public Map<String, String> updateInventory(
            @RequestBody CombinedRequest request) {

        Map<String, String> response =
                new HashMap<>();

        try {

            Product product =
                    request.getProduct();

            Inventory inventory =
                    request.getInventory();

            boolean valid =
                    serviceClass
                    .ValidateProductId(
                            product.getId());

            if (!valid) {

                response.put(
                        "message",
                        "Product not found"
                );

                return response;
            }

            Inventory existingInventory =
                    serviceClass
                    .getInventoryId(
                            inventory);

            if (existingInventory != null) {

                productRepository
                        .save(product);

                existingInventory.setQuantity(
                        inventory.getQuantity());

                inventoryRepository
                        .save(existingInventory);

                response.put(
                        "message",
                        "Successfully updated product"
                );

            } else {

                response.put(
                        "message",
                        "No data available"
                );
            }

        } catch (
                DataIntegrityViolationException e) {

            response.put(
                    "message",
                    e.getMessage()
            );

        } catch (Exception e) {

            response.put(
                    "message",
                    e.getMessage()
            );
        }

        return response;
    }

    // Save Inventory
    @PostMapping
    public Map<String, String> saveInventory(
            @RequestBody Inventory inventory) {

        Map<String, String> response =
                new HashMap<>();

        try {

            boolean valid =
                    serviceClass
                    .validateInventory(
                            inventory);

            if (!valid) {

                response.put(
                        "message",
                        "Data already present"
                );

                return response;
            }

            inventoryRepository
                    .save(inventory);

            response.put(
                    "message",
                    "Data saved successfully"
            );

        } catch (
                DataIntegrityViolationException e) {

            response.put(
                    "message",
                    e.getMessage()
            );

        } catch (Exception e) {

            response.put(
                    "message",
                    e.getMessage()
            );
        }

        return response;
    }

    // Get All Products By StoreId
    @GetMapping("/{storeid}")
    public Map<String, Object> getAllProducts(
            @PathVariable Long storeid) {

        Map<String, Object> response =
                new HashMap<>();

        List<Product> products =
                productRepository
                .findProductsByStoreId(
                        storeid);

        response.put(
                "products",
                products
        );

        return response;
    }

    // Filter Product
    @GetMapping(
        "/filter/{category}/{name}/{storeid}"
    )
    public Map<String, Object>
    getProductName(
            @PathVariable String category,
            @PathVariable String name,
            @PathVariable Long storeid) {

        Map<String, Object> response =
                new HashMap<>();

        List<Product> products;

        // category null → search by name
        if ("null".equals(category)) {

            products =
                    productRepository
                    .findByNameLike(
                            storeid,
                            name);

        }

        // name null → search by category
        else if ("null".equals(name)) {

            products =
                    productRepository
                    .findByCategoryAndStoreId(
                            category,
                            storeid);

        }

        // both provided
        else {

            products =
                    productRepository
                    .findByNameAndCategory(
                            storeid,
                            name,
                            category);
        }

        response.put(
                "product",
                products
        );

        return response;
    }

    // Search Product
    @GetMapping(
        "/search/{name}/{storeId}"
    )
    public Map<String, Object>
    searchProduct(
            @PathVariable String name,
            @PathVariable Long storeId) {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "product",
                productRepository
                .findByNameLike(
                        storeId,
                        name)
        );

        return response;
    }

    // Remove Product
    @DeleteMapping("/{id}")
    public Map<String, String> removeProduct(
            @PathVariable Long id) {

        Map<String, String> response =
                new HashMap<>();

        boolean valid =
                serviceClass
                .ValidateProductId(id);

        if (!valid) {

            response.put(
                    "message",
                    "Product not present in database"
            );

            return response;
        }

        inventoryRepository
                .deleteByProductId(id);

        response.put(
                "message",
                "Product deleted successfully"
        );

        return response;
    }

    // Validate Quantity
    @GetMapping(
        "/validate/{quantity}/{storeId}/{productId}"
    )
    public boolean validateQuantity(
            @PathVariable Integer quantity,
            @PathVariable Long storeId,
            @PathVariable Long productId) {

        Inventory inventory =
                inventoryRepository
                .findByProductIdandStoreId(
                        productId,
                        storeId);

        if (inventory == null) {
            return false;
        }

        return inventory.getQuantity()
                >= quantity;
    }
}
