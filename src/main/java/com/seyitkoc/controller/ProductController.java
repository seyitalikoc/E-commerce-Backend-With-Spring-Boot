package com.seyitkoc.controller;

import com.seyitkoc.dto.DtoProduct;
import com.seyitkoc.dto.DtoProductIU;
import com.seyitkoc.entity.RootEntity;
import com.seyitkoc.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/getList")
    RootEntity<List<DtoProduct>> getAllProductWithParam(@RequestParam(value = "mainCat", required = false) String mainCategory,
                                                        @RequestParam(value = "subCat", required = false) String subCategory){
        return RootEntity.ok(productService.getAllProductWithParam(mainCategory, subCategory));
    }

    @GetMapping("/search")
    RootEntity<List<DtoProduct>> getAllSearchingProductList(@RequestParam(value = "q", required = false) String q){
        return RootEntity.ok(productService.getAllSearchingProductList(q));
    }

    @GetMapping("/filter")
    RootEntity<List<DtoProduct>> getAllProductsWithPriceFilter(@RequestParam(value = "mainCat", required = false) String mainCategory,
                                                               @RequestParam(value = "subCat", required = false) String subCategory,
                                                               @RequestParam(value = "price_min", required = false) Double price_min,
                                                               @RequestParam(value = "price_max", required = false) Double price_max,
                                                               @RequestParam(value = "sort") String sort){

        return RootEntity.ok(productService.getAllProductsWithFilter(mainCategory, subCategory, price_min, price_max, sort));
    }

    @GetMapping("/get")
    RootEntity<DtoProduct> getProductById(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "name", required = false) String name){
        return RootEntity.ok(productService.getProductWithParams(id,name));
    }


    @PostMapping("/save")
    RootEntity<DtoProduct> saveProduct(@RequestBody DtoProductIU dtoProductIU){
        return RootEntity.ok(productService.saveProduct(dtoProductIU));
    }

    @PutMapping("/update")
    RootEntity<DtoProduct> updateProduct(@RequestParam(value = "id") Long id ,@RequestBody DtoProductIU dtoProductIU){
        return RootEntity.ok(productService.updateProduct( id, dtoProductIU));
    }

    @DeleteMapping("/delete")
    RootEntity<String> deleteProduct(@RequestParam(value = "id") Long id){
        return RootEntity.ok(productService.deleteProductById( id));
    }

}
