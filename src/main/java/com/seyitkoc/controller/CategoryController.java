package com.seyitkoc.controller;

import com.seyitkoc.dto.DtoSubCategory;
import com.seyitkoc.entity.RootEntity;
import com.seyitkoc.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/api/category/")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/getSubCategories")
    public RootEntity<List<DtoSubCategory>> getSubCategoriesFromName(@RequestParam(value = "category") String category){
        return RootEntity.ok(categoryService.getSubCategoriesWithName(category));
    }

    @GetMapping("/get")
    public RootEntity<DtoSubCategory> getSubCategoryFromName(@RequestParam(value = "category") String category){
        return RootEntity.ok(categoryService.getSubCategoryFromName(category));
    }
}
