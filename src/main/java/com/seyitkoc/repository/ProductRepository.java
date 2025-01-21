package com.seyitkoc.repository;

import com.seyitkoc.entity.Product;
import com.seyitkoc.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> getProductsBySubCategory(SubCategory subCategory);

    List<Product> findAllBySubCategory(SubCategory subCategory);

    Optional<Product> getProductById(Long id);

    Optional<Product> getProductByProductName(String productName);

    @Query("SELECT p FROM Product p WHERE p.description LIKE %:keyword% OR p.productName LIKE %:keyword%")
    List<Product> findByDescriptionOrProductNameContaining(String keyword);

    @Query("select p from Product p where p.subCategory = :subCategory and (:price_min is null or p.price >= :price_min) and (:price_max is null or p.price <= :price_max)")
    List<Product> findProductsByFilter(SubCategory subCategory, BigDecimal price_min, BigDecimal price_max);

}
