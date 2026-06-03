package com.project.code.Controller;

package com.project.code.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.project.code.Model.Product;
import com.project.code.Repository.InventoryRepository;
import com.project.code.Repository.ProductRepository;
import com.project.code.Service.ServiceClass;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ServiceClass serviceClass;

    @Autowired
    private InventoryRepository inventoryRepository;

    // Add Product
    @PostMapping
    public Map<String, String> addProduct(
            @RequestBody Product product) {

        Map<String, String> response = new HashMap<>();

        try {

            boolean isValid = serviceClass
                    .validateProduct(product);

            if (!isValid) {
                response.put(
                        "message",
                        "Product already exists"
                );
                return response;
            }

            productRepository.save(product);

            response.put(
                    "message",
                    "Product added successfully"
            );

        } catch (DataIntegrityViolationException e) {

            response.put(
                    "message",
                    "SKU already exists"
            );

        } catch (Exception e) {

            response.put(
                    "message",
                    e.getMessage()
            );
        }

        return response;
    }

    // Get Product By Id
    @GetMapping("/product/{id}")
    public Map<String, Object> getProductbyId(
            @PathVariable Long id) {

        Map<String, Object> response =
                new HashMap<>();

        Optional<Product> product =
                productRepository.findById(id);

        response.put("products",
                product.orElse(null));

        return response;
    }

    // Update Product
    @PutMapping
    public Map<String, String> updateProduct(
            @RequestBody Product product) {

        Map<String, String> response =
                new HashMap<>();

        try {

            productRepository.save(product);

            response.put(
                    "message",
                    "Product updated successfully"
            );

        } catch (Exception e) {

            response.put(
                    "message",
                    e.getMessage()
            );
        }

        return response;
    }

    // Filter By Category & Name
    @GetMapping("/category/{name}/{category}")
    public Map<String, Object>
    filterbyCategoryProduct(
            @PathVariable String name,
            @PathVariable String category) {

        Map<String, Object> response =
                new HashMap<>();

        List<Product> products;

        if ("null".equals(name)
                && "null".equals(category)) {

            products = productRepository.findAll();

        } else if ("null".equals(name)) {

            products = productRepository
                    .findByCategory(category);

        } else if ("null".equals(category)) {

            products = productRepository
                    .findProductBySubName(name);

        } else {

            products = productRepository
                    .findProductBySubNameAndCategory(
                            name, category);
        }

        response.put("products", products);

        return response;
    }

    // List All Products
    @GetMapping
    public Map<String, Object> listProduct() {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "products",
                productRepository.findAll()
        );

        return response;
    }

    // Filter Product By Category & Store Id
    @GetMapping("/filter/{category}/{storeid}")
    public Map<String, Object>
    getProductbyCategoryAndStoreId(
            @PathVariable String category,
            @PathVariable Long storeid) {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "product",
                productRepository
                        .findProductByCategory(
                                category,
                                storeid)
        );

        return response;
    }

    // Delete Product
    @DeleteMapping("/{id}")
    public Map<String, String> deleteProduct(
            @PathVariable Long id) {

        Map<String, String> response =
                new HashMap<>();

        try {

            boolean exists =
                    serviceClass
                    .ValidateProductId(id);

            if (!exists) {

                response.put(
                        "message",
                        "Product not found"
                );

                return response;
            }

            inventoryRepository
                    .deleteByProductId(id);

            productRepository
                    .deleteById(id);

            response.put(
                    "message",
                    "Product deleted successfully"
            );

        } catch (Exception e) {

            response.put(
                    "message",
                    e.getMessage()
            );
        }

        return response;
    }

    // Search Product
    @GetMapping("/searchProduct/{name}")
    public Map<String, Object>
    searchProduct(
            @PathVariable String name) {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "products",
                productRepository
                        .findProductBySubName(name)
        );

        return response;
    }
}
}
