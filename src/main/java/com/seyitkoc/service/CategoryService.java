package com.seyitkoc.service;

import com.seyitkoc.dto.DtoSubCategory;
import com.seyitkoc.entity.MainCategory;
import com.seyitkoc.entity.SubCategory;
import com.seyitkoc.exception.BaseException;
import com.seyitkoc.exception.ErrorMessage;
import com.seyitkoc.exception.MessageType;
import com.seyitkoc.mapper.SubCategoryMapper;
import com.seyitkoc.repository.MainCategoryRepository;
import com.seyitkoc.repository.SubCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final MainCategoryRepository mainCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final SubCategoryMapper subCategoryMapper;

    public CategoryService(SubCategoryMapper subCategoryMapper, MainCategoryRepository mainCategoryRepository, SubCategoryRepository subCategoryRepository) {
        this.subCategoryMapper = subCategoryMapper;
        this.mainCategoryRepository = mainCategoryRepository;
        this.subCategoryRepository = subCategoryRepository;
    }

    // Get List of SubCategory
    public List<DtoSubCategory> getSubCategoriesWithName(String categoryName){
        MainCategory mainCategory = findMainCategoryByCategoryName(categoryName);
        return  subCategoryMapper.toDtoSubCategories(mainCategory.getSubCategories());
    }
    private MainCategory findMainCategoryByCategoryName(String categoryName) {
        return mainCategoryRepository.findMainCategoryByCategoryName(categoryName)
                .orElseThrow(() -> new BaseException(new ErrorMessage(
                        MessageType.NO_RECORD_EXIST, String.format("No main category found with name: %s", categoryName))));
    }


    // Get SubCategory
    public DtoSubCategory getSubCategoryFromName(String categoryName) {
        SubCategory subCategory = findSubCategoryByCategoryName(categoryName);
        return subCategoryMapper.toDtoSubCategory(subCategory);
    }
    private SubCategory findSubCategoryByCategoryName(String categoryName){
        return subCategoryRepository.findSubCategoryByCategoryName(categoryName)
                .orElseThrow(() -> new BaseException(new ErrorMessage(
                        MessageType.NO_RECORD_EXIST, String.format("No sub category found with name: %s", categoryName))));
    }
}
