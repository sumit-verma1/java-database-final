package com.project.code.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.code.DTO.PlaceOrderRequestDTO;
import com.project.code.Model.Customer;
import com.project.code.Model.Inventory;
import com.project.code.Model.OrderDetails;
import com.project.code.Model.OrderItem;
import com.project.code.Model.Product;
import com.project.code.Model.PurchaseProduct;
import com.project.code.Model.Store;
import com.project.code.Repository.CustomerRepository;
import com.project.code.Repository.InventoryRepository;
import com.project.code.Repository.OrderDetailsRepository;
import com.project.code.Repository.OrderItemRepository;
import com.project.code.Repository.ProductRepository;
import com.project.code.Repository.StoreRepository;

@Service
public class OrderService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    public void saveOrder(
            PlaceOrderRequestDTO placeOrderRequest) {

        // Find existing customer
        Optional<Customer> customerOptional =
                customerRepository.findByEmail(
                        placeOrderRequest.getEmail());

        Customer customer;

        // Create customer if not found
        if (customerOptional.isPresent()) {

            customer =
                    customerOptional.get();

        } else {

            customer =
                    new Customer();

            customer.setName(
                    placeOrderRequest.getName());

            customer.setEmail(
                    placeOrderRequest.getEmail());

            customer.setPhone(
                    placeOrderRequest.getPhone());

            customer =
                    customerRepository
                    .save(customer);
        }

        // Get store
        Optional<Store> storeOptional =
                storeRepository.findById(
                        placeOrderRequest.getStoreId());

        if (!storeOptional.isPresent()) {

            throw new RuntimeException(
                    "Store not found");
        }

        Store store =
                storeOptional.get();

        // Create order details
        OrderDetails orderDetails =
                new OrderDetails();

        orderDetails.setCustomer(
                customer);

        orderDetails.setStore(
                store);

        orderDetails.setTotalPrice(
                placeOrderRequest
                        .getTotalPrice());

        orderDetails.setDate(
                LocalDateTime.now());

        // Save order details
        orderDetails =
                orderDetailsRepository
                .save(orderDetails);

        // Process products
        for (PurchaseProduct productDTO
                : placeOrderRequest
                .getPurchaseProduct()) {

            // Find inventory
            Inventory inventory =
                    inventoryRepository
                    .findByProductIdandStoreId(
                            productDTO
                                    .getProductId(),
                            placeOrderRequest
                                    .getStoreId());

            if (inventory == null) {

                throw new RuntimeException(
                        "Inventory not found");
            }

            // Reduce stock level
            inventory.setQuantity(
                    inventory.getQuantity()
                    - productDTO
                    .getQuantity());

            // Save updated inventory
            inventoryRepository
                    .save(inventory);

            // Find product
            Product product =
                    productRepository
                    .findByid(
                            productDTO
                                    .getProductId());

            // Create order item
            OrderItem orderItem =
                    new OrderItem();

            orderItem.setOrder(
                    orderDetails);

            orderItem.setProduct(
                    product);

            orderItem.setQuantity(
                    productDTO
                            .getQuantity());

            orderItem.setPrice(
                    product.getPrice());

            // Save order item
            orderItemRepository
                    .save(orderItem);
        }
    }
}
