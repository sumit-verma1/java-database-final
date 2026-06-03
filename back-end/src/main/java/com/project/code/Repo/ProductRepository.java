package com.project.code.Repo;




import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.code.Model.Product;

@Repository
public interface ProductRepository
        extends JpaRepository<Product, Long> {

    // Find All Products
    List<Product> findAll();

    // Find By Category
    List<Product> findByCategory(
            String category);

    // Find By Price Range
    List<Product> findByPriceBetween(
            Double minPrice,
            Double maxPrice);

    // Find By SKU
    List<Product> findBySku(
            String sku);

    // Find By Name
    Product findByName(
            String name);

    // Find By Id
    Product findByid(
            Long id);

    // Find Product By Name for Store
    @Query("""
        SELECT i.product
        FROM Inventory i
        WHERE i.store.id = :storeId
        AND LOWER(i.product.name)
        LIKE LOWER(CONCAT('%', :pname, '%'))
    """)
    List<Product> findByNameLike(
            @Param("storeId") Long storeId,
            @Param("pname") String pname);

    // Find By Name & Category
    @Query("""
        SELECT i.product
        FROM Inventory i
        WHERE i.store.id = :storeId
        AND LOWER(i.product.name)
        LIKE LOWER(CONCAT('%', :pname, '%'))
        AND i.product.category = :category
    """)
    List<Product> findByNameAndCategory(
            @Param("storeId") Long storeId,
            @Param("pname") String pname,
            @Param("category") String category);

    // Find By Category & StoreId
    @Query("""
        SELECT i.product
        FROM Inventory i
        WHERE i.store.id = :storeId
        AND i.product.category = :category
    """)
    List<Product> findByCategoryAndStoreId(
            @Param("category") String category,
            @Param("storeId") Long storeId);

    // Find Product By Sub Name
    @Query("""
        SELECT i
        FROM Product i
        WHERE LOWER(i.name)
        LIKE LOWER(CONCAT('%', :pname, '%'))
    """)
    List<Product> findProductBySubName(
            @Param("pname") String pname);

    // Find Products By Store Id
    @Query("""
        SELECT i.product
        FROM Inventory i
        WHERE i.store.id = :storeId
    """)
    List<Product> findProductsByStoreId(
            @Param("storeId") Long storeId);

    // Find Product By Category
    @Query("""
        SELECT i.product
        FROM Inventory i
        WHERE i.product.category = :category
        AND i.store.id = :storeId
    """)
    List<Product> findProductByCategory(
            @Param("category") String category,
            @Param("storeId") Long storeId);

    // Find Product By Name & Category
    @Query("""
        SELECT i
        FROM Product i
        WHERE LOWER(i.name)
        LIKE LOWER(CONCAT('%', :pname, '%'))
        AND i.category = :category
    """)
    List<Product> findProductBySubNameAndCategory(
            @Param("pname") String pname,
            @Param("category") String category);
}
}
