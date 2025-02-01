package com.seyitkoc.controller;

import com.seyitkoc.dto.DtoProduct;
import com.seyitkoc.dto.DtoProductIU;
import com.seyitkoc.entity.RootEntity;
import com.seyitkoc.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/search")
    RootEntity<Page<DtoProduct>> getAllSearchingProductList(@RequestParam(value = "q", required = false) String q,
                                                            @RequestParam(value = "page", required = false) Integer page){
        return RootEntity.ok(productService.getAllSearchingProductList(q, page));
    }

    @GetMapping("/filter")
    RootEntity<Page<DtoProduct>> getAllProductsWithPriceFilter(@RequestParam(value = "category", required = false) String category,
                                                               @RequestParam(value = "price_min", required = false) Double price_min,
                                                               @RequestParam(value = "price_max", required = false) Double price_max,
                                                               @RequestParam(value = "page", required = false) Integer page,
                                                               @RequestParam(value = "sort", required = false) String sort){

        return RootEntity.ok(productService.getAllProductsWithFilter(category, price_min, price_max, sort, page));
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
