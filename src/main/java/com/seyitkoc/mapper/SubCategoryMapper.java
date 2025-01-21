package com.seyitkoc.mapper;

import com.seyitkoc.dto.DtoMainCategory;
import com.seyitkoc.dto.DtoSubCategory;
import com.seyitkoc.entity.SubCategory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubCategoryMapper {

    public List<DtoSubCategory> toDtoSubCategories(List<SubCategory> subCategories){
        return subCategories.stream()
                .map(this::toDtoSubCategory)
                .collect(Collectors.toList());
    }

    public DtoSubCategory toDtoSubCategory(SubCategory subCategory){
        DtoSubCategory dtoSubCategory = new DtoSubCategory();
        DtoMainCategory dtoMainCategory = new DtoMainCategory();
        BeanUtils.copyProperties(subCategory.getMainCategory(),dtoMainCategory);
        BeanUtils.copyProperties(subCategory,dtoSubCategory);
        dtoSubCategory.setMainCategory(dtoMainCategory);
        return dtoSubCategory;
    }
}
