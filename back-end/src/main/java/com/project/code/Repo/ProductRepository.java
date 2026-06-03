package com.project.code.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.code.Model.Product;

@Repository
public interface ProductRepository
        extends JpaRepository<Product, Long> {

    // Find all products
    List<Product> findAll();

    // Find by category
    List<Product> findByCategory(
            String category);

    // Find by price range
    List<Product> findByPriceBetween(
            Double minPrice,
            Double maxPrice);

    // Find by SKU
    List<Product> findBySku(
            String sku);

    // Find by name
    Product findByName(
            String name);

    // Find by ID
    Product findByid(
            Long id);

    // Find products by name for store
    @Query("""
        SELECT i.product
        FROM Inventory i
        WHERE i.store.id = :storeId
        AND LOWER(i.product.name)
        LIKE LOWER(CONCAT('%', :pname, '%'))
    """)
    List<Product> findByNameLike(
            Long storeId,
            String pname);

    // Find by name and category for store
    @Query("""
        SELECT i.product
        FROM Inventory i
        WHERE i.store.id = :storeId
        AND LOWER(i.product.name)
        LIKE LOWER(CONCAT('%', :pname, '%'))
        AND i.product.category = :category
    """)
    List<Product> findByNameAndCategory(
            Long storeId,
            String pname,
            String category);

    // Find by category and store id
    @Query("""
        SELECT i.product
        FROM Inventory i
        WHERE i.store.id = :storeId
        AND i.product.category = :category
    """)
    List<Product> findByCategoryAndStoreId(
            String category,
            Long storeId);

    // Find product by name
    @Query("""
        SELECT i
        FROM Product i
        WHERE LOWER(i.name)
        LIKE LOWER(CONCAT('%', :pname, '%'))
    """)
    List<Product> findProductBySubName(
            String pname);

    // Find products by store id
    @Query("""
        SELECT i.product
        FROM Inventory i
        WHERE i.store.id = :storeId
    """)
    List<Product> findProductsByStoreId(
            Long storeId);

    // Find product by category & store
    @Query("""
        SELECT i.product
        FROM Inventory i
        WHERE i.product.category = :category
        AND i.store.id = :storeId
    """)
    List<Product> findProductByCategory(
            String category,
            Long storeId);

    // Find product by name & category
    @Query("""
        SELECT i
        FROM Product i
        WHERE LOWER(i.name)
        LIKE LOWER(CONCAT('%', :pname, '%'))
        AND i.category = :category
    """)
    List<Product> findProductBySubNameAndCategory(
            String pname,
            String category);
}
