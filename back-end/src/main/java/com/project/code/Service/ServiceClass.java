package com.project.code.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Repository.InventoryRepository;
import com.project.code.Repository.ProductRepository;

@Service
public class ServiceClass {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    // validateInventory
    public boolean validateInventory(
            Inventory inventory) {

        Inventory inventoryData =
                inventoryRepository
                .findByProductIdandStoreId(
                        inventory
                                .getProduct()
                                .getId(),
                        inventory
                                .getStore()
                                .getId());

        if (inventoryData != null) {
            return false;
        }

        return true;
    }

    // validateProduct
    public boolean validateProduct(
            Product product) {

        Product productData =
                productRepository
                .findByName(
                        product.getName());

        if (productData != null) {
            return false;
        }

        return true;
    }

    // ValidateProductId
    public boolean ValidateProductId(
            long id) {

        Product product =
                productRepository
                .findByid(id);

        if (product == null) {
            return false;
        }

        return true;
    }

    // getInventoryId
    public Inventory getInventoryId(
            Inventory inventory) {

        Inventory inventoryData =
                inventoryRepository
                .findByProductIdandStoreId(
                        inventory
                                .getProduct()
                                .getId(),
                        inventory
                                .getStore()
                                .getId());

        return inventoryData;
    }
}
