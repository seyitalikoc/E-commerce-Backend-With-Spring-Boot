package com.seyitkoc.repository;

import com.seyitkoc.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Page<Product> findAll(Specification<Product> spec, Pageable pageable);

    Optional<Product> getProductById(Long id);

    Optional<Product> getProductByProductName(String productName);

    @Query("SELECT p FROM Product p WHERE p.description LIKE %:keyword% OR p.productName LIKE %:keyword%")
    List<Product> findByDescriptionOrProductNameContaining(String keyword);

    /*@Query("select p from Product p where p.category = :subCategory and (:price_min is null or p.price >= :price_min) and (:price_max is null or p.price <= :price_max)")
    List<Product> findProductsByFilter(SubCategory subCategory, BigDecimal price_min, BigDecimal price_max);*/

}
