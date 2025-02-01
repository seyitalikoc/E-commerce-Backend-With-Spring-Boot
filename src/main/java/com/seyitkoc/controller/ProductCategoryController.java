package com.seyitkoc.controller;

import com.seyitkoc.dto.DtoProductCategory;
import com.seyitkoc.entity.RootEntity;
import com.seyitkoc.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/api/category/")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("/get")
    public RootEntity<DtoProductCategory> getSubCategoryFromName(@RequestParam(value = "category") String category_slug){
        return RootEntity.ok(productCategoryService.getCategoryByName(category_slug));
    }

}
