package com.seyitkoc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "product_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug; // SEO friendly URL

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private ProductCategory parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<ProductCategory> subCategories = new ArrayList<>();

    @Column(nullable = false)
    private boolean active = true;

    // Timestamps
    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    // Bidirectional relationship with products
    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    // Helper methods for managing subcategories
    public void addSubCategory(ProductCategory subCategory) {
        subCategories.add(subCategory);
        subCategory.setParent(this);
    }

    public void removeSubCategory(ProductCategory subCategory) {
        subCategories.remove(subCategory);
        subCategory.setParent(null);
    }

    // Helper method to check if category is a root category
    public boolean isRootCategory() {
        return parent == null;
    }

    // Helper method to check if category has subcategories
    public boolean hasSubCategories() {
        return !subCategories.isEmpty();
    }
}
