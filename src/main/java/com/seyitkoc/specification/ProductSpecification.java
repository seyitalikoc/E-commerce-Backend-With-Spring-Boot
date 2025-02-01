package com.seyitkoc.specification;

import com.seyitkoc.entity.Product;
import com.seyitkoc.entity.ProductCategory;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {


    public static Specification<Product> filterByCategory(String category) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, ProductCategory> categoryJoin = root.join("category");
            return criteriaBuilder.equal(categoryJoin.get("slug"), category);
        };
    }


    public static Specification<Product> nameContains(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("productName"), "%" + keyword + "%");
    }
    public static Specification<Product> descriptionContains(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("description"), "%" + keyword + "%");
    }
    public static Specification<Product> categoryContains(String keyword) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, ProductCategory> categoryJoin = root.join("category");
            return criteriaBuilder.like(categoryJoin.get("slug"), "%" + keyword + "%");
        };
    }


    public static Specification<Product> categoryIs(String category){
        return (root, query, criteriaBuilder) -> {
            Join<Product, ProductCategory> categoryJoin = root.join("category");
            return criteriaBuilder.equal(categoryJoin.get("slug"), category);
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
