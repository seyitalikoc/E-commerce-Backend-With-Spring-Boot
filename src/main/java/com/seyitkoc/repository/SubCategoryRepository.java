package com.seyitkoc.repository;

import com.seyitkoc.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    Optional<SubCategory> getSubCategoryByCategoryName(String categoryName);

    Long id(Long id);

    Optional<SubCategory> findSubCategoryByCategoryName(String categoryName);

    @Query("SELECT p FROM SubCategory p WHERE p.categoryName LIKE %:keyword% ")
    List<SubCategory> findByKeyword(String keyword);
}
