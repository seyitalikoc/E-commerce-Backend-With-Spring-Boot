package com.seyitkoc.repository;

import com.seyitkoc.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    Optional<ProductCategory> findProductCategoryBySlug(String slug);
}
