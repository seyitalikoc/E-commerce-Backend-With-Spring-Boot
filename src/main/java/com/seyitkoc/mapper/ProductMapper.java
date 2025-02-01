package com.seyitkoc.mapper;

import com.seyitkoc.dto.DtoProduct;
import com.seyitkoc.dto.DtoProductIU;
import com.seyitkoc.entity.Product;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public List<DtoProduct> toDtoProducts(List<Product> products) {
        return products.stream()
                .map(this::toDtoProduct)
                .collect(Collectors.toList());
    }

    public DtoProduct toDtoProduct(Product product) {
        DtoProduct dtoProduct = new DtoProduct();
        //DtoSubCategory dtoSubCategory = new DtoSubCategory();
        BeanUtils.copyProperties(product, dtoProduct);
        //BeanUtils.copyProperties(product.getProductCategory(), dtoSubCategory);

        //dtoProduct.setSubCategory(dtoSubCategory);
        return dtoProduct;
    }

    public Product toEntity(DtoProductIU dtoProductIU) {
        Product product = new Product();
        BeanUtils.copyProperties(dtoProductIU, product);
        return product;
    }
}
