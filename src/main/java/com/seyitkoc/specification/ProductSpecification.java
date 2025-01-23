package com.seyitkoc.specification;

import com.seyitkoc.entity.MainCategory;
import com.seyitkoc.entity.Product;
import com.seyitkoc.entity.SubCategory;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {

    public static Specification<Product> filterByMainCategory(String mainCategory) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, SubCategory> subCategoryJoin = root.join("subCategory");
            Join<SubCategory, MainCategory> mainCategoryJoin = subCategoryJoin.join("mainCategory");
            return criteriaBuilder.equal(mainCategoryJoin.get("categoryName"), mainCategory);
        };
    }
    public static Specification<Product> filterBySubCategory(String subCategory) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, SubCategory> subCategoryJoin = root.join("subCategory");
            return criteriaBuilder.equal(subCategoryJoin.get("categoryName"), subCategory);
        };
    }


    public static Specification<Product> nameContains(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("productName"), "%" + keyword + "%");
    }
    public static Specification<Product> descriptionContains(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("description"), "%" + keyword + "%");
    }
    public static Specification<Product> mainCategoryContains(String keyword) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, SubCategory> subCategoryJoin = root.join("subCategory");
            Join<SubCategory, MainCategory> mainCategoryJoin = subCategoryJoin.join("mainCategory");
            return criteriaBuilder.like(mainCategoryJoin.get("categoryName"), "%" + keyword + "%");
        };
    }
    public static Specification<Product> subCategoryContains(String keyword) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, SubCategory> subCategoryJoin = root.join("subCategory");
            return criteriaBuilder.like(subCategoryJoin.get("categoryName"), "%" + keyword + "%");
        };
    }


    public static Specification<Product> mainCategoryIs(String mainCategory){
        return (root, query, criteriaBuilder) -> {
            Join<Product, SubCategory> subCategoryJoin = root.join("subCategory");
            Join<SubCategory, MainCategory> mainCategoryJoin = subCategoryJoin.join("mainCategory");
            return criteriaBuilder.equal(mainCategoryJoin.get("categoryName"), mainCategory);
        };
    }
    public static Specification<Product> subCategoryIs(String subCategory){
        return (root, query, criteriaBuilder) -> {
            Join<Product, SubCategory> subCategoryJoin = root.join("subCategory");
            return criteriaBuilder.equal(subCategoryJoin.get("categoryName"), subCategory);
        };
    }
    public static Specification<Product> priceGreaterThenOrEqual(BigDecimal price){
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"), price);
    }
    public static Specification<Product> priceLessThenOrEqual(BigDecimal price){
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), price);
    }


    public static Specification<Product> hasId(Long id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
    }
    public static Specification<Product> hasName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("productName"), name);
    }
}
