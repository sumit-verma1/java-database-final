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
import com.project.code.Repository.StoreRepository;

@Service
public class OrderService {

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

        // Find Existing Customer
        Optional<Customer> existingCustomer =
                customerRepository.findByEmail(
                        placeOrderRequest
                                .getEmail());

        Customer customer;

        // Create Customer If Not Exists
        if (existingCustomer.isPresent()) {

            customer = existingCustomer.get();

        } else {

            customer = new Customer();

            customer.setName(
                    placeOrderRequest
                            .getName());

            customer.setEmail(
                    placeOrderRequest
                            .getEmail());

            customer.setPhone(
                    placeOrderRequest
                            .getPhone());

            customer =
                    customerRepository
                    .save(customer);
        }

        // Find Store
        Optional<Store> optionalStore =
                storeRepository.findById(
                        placeOrderRequest
                                .getStoreId());

        if (!optionalStore.isPresent()) {
            throw new RuntimeException(
                    "Store not found");
        }

        Store store = optionalStore.get();

        // Create Order
        OrderDetails orderDetails =
                new OrderDetails();

        orderDetails.setCustomer(customer);
        orderDetails.setStore(store);
        orderDetails.setTotalPrice(
                placeOrderRequest
                        .getTotalPrice());

        orderDetails.setDate(
                LocalDateTime.now());

        orderDetails =
                orderDetailsRepository
                .save(orderDetails);

        // Save Order Items
        for (PurchaseProduct purchase :
                placeOrderRequest
                        .getPurchaseProduct()) {

            Inventory inventory =
                    inventoryRepository
                    .findByProductIdandStoreId(
                            purchase
                                    .getProductId(),
                            placeOrderRequest
                                    .getStoreId());

            if (inventory == null) {
                throw new RuntimeException(
                        "Inventory not found");
            }

            // Update Stock
            inventory.setQuantity(
                    inventory.getQuantity()
                            - purchase.getQuantity());

            inventoryRepository
                    .save(inventory);

            // Create Order Item
            OrderItem orderItem =
                    new OrderItem();

            Product product =
                    inventory.getProduct();

            orderItem.setOrder(
                    orderDetails);

            orderItem.setProduct(
                    product);

            orderItem.setQuantity(
                    purchase.getQuantity());

            orderItem.setPrice(
                    product.getPrice());

            orderItemRepository
                    .save(orderItem);
        }
    }
}
