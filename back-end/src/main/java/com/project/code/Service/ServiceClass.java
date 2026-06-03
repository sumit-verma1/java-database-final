package com.project.code.Service;


package com.project.code.Service;

import org.springframework.stereotype.Service;

import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Repository.InventoryRepository;
import com.project.code.Repository.ProductRepository;

@Service
public class ServiceClass {

    private final ProductRepository productRepository;

    private final InventoryRepository inventoryRepository;

    public ServiceClass(
            ProductRepository productRepository,
            InventoryRepository inventoryRepository) {

        this.productRepository =
                productRepository;

        this.inventoryRepository =
                inventoryRepository;
    }

    // Validate Inventory
    public boolean validateInventory(
            Inventory inventory) {

        Inventory existingInventory =
                inventoryRepository
                .findByProductIdandStoreId(
                        inventory
                                .getProduct()
                                .getId(),
                        inventory
                                .getStore()
                                .getId());

        return existingInventory == null;
    }

    // Validate Product
    public boolean validateProduct(
            Product product) {

        Product existingProduct =
                productRepository
                .findByName(
                        product.getName());

        return existingProduct == null;
    }

    // Validate Product Id
    public boolean ValidateProductId(
            long id) {

        Product product =
                productRepository
                .findByid(id);

        return product != null;
    }

    // Get Inventory Id
    public Inventory getInventoryId(
            Inventory inventory) {

        return inventoryRepository
                .findByProductIdandStoreId(
                        inventory
                                .getProduct()
                                .getId(),
                        inventory
                                .getStore()
                                .getId());
    }
}
