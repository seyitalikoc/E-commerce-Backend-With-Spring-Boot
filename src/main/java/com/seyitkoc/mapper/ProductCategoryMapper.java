package com.seyitkoc.mapper;

import com.seyitkoc.dto.DtoProductCategory;
import com.seyitkoc.entity.ProductCategory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductCategoryMapper {

    public List<DtoProductCategory> toDtoProductCategoryList(List<ProductCategory> productCategoryList) {
        return productCategoryList.stream().map(this::toDtoProductCategory).toList();
    }

    public DtoProductCategory toDtoProductCategory(ProductCategory productCategory) {
        DtoProductCategory dtoProductCategory = new DtoProductCategory();
        BeanUtils.copyProperties(productCategory, dtoProductCategory);
        if(productCategory.getProducts() != null) {
            dtoProductCategory.setProducts(new ProductMapper().toDtoProducts(productCategory.getProducts()));
        }
        if (productCategory.getParent() != null) {
            DtoProductCategory parent = new DtoProductCategory();
            BeanUtils.copyProperties(productCategory.getParent(), parent);
            dtoProductCategory.setParent(parent);
        }
        if(productCategory.getSubCategories() != null) {

            dtoProductCategory.setSubCategories(toDtoProductCategoryList(productCategory.getSubCategories()));
        }
        if (productCategory.getProducts() != null) {
            dtoProductCategory.setProducts(new ProductMapper().toDtoProducts(productCategory.getProducts()));
        }
        return dtoProductCategory;
    }
}
