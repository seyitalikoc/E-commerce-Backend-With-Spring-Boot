package com.seyitkoc.repository;

import com.seyitkoc.entity.MainCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MainCategoryRepository extends JpaRepository<MainCategory, Long> {
    Optional<MainCategory> getMainCategoryByCategoryName(String mainCategory);

    @Query("SELECT p FROM MainCategory p WHERE p.categoryName LIKE %:keyword%")
    List<MainCategory> findByKeyword(String keyword);

    Optional<MainCategory> findMainCategoryByCategoryName(String categoryName);
}
