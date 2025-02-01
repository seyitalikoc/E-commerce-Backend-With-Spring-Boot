package com.seyitkoc.service;

import com.seyitkoc.dto.DtoProductCategory;
import com.seyitkoc.entity.ProductCategory;
import com.seyitkoc.exception.BaseException;
import com.seyitkoc.exception.ErrorMessage;
import com.seyitkoc.exception.MessageType;
import com.seyitkoc.mapper.ProductCategoryMapper;
import com.seyitkoc.repository.ProductCategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryService {


    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryMapper productCategoryMapper;

    public ProductCategoryService(ProductCategoryRepository productCategoryRepository, ProductCategoryMapper productCategoryMapper) {
        this.productCategoryRepository = productCategoryRepository;
        this.productCategoryMapper = productCategoryMapper;
    }


    public DtoProductCategory getCategoryByName(String category_slug) {
        return productCategoryMapper.toDtoProductCategory(getSubCategoryByName(category_slug));
    }
    private ProductCategory getSubCategoryByName(String category_slug) {
        return productCategoryRepository.findProductCategoryBySlug(category_slug)
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, "Category Not Found")));
    }

}
