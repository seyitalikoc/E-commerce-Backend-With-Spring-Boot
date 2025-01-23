package com.seyitkoc.service;

import com.seyitkoc.dto.DtoProduct;
import com.seyitkoc.dto.DtoProductIU;
import com.seyitkoc.entity.Product;
import com.seyitkoc.entity.SubCategory;
import com.seyitkoc.exception.BaseException;
import com.seyitkoc.exception.ErrorMessage;
import com.seyitkoc.exception.MessageType;
import com.seyitkoc.mapper.ProductMapper;
import com.seyitkoc.repository.ProductRepository;
import com.seyitkoc.repository.SubCategoryRepository;
import com.seyitkoc.specification.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository,
                          SubCategoryRepository subCategoryRepository,
                          ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.productMapper = productMapper;
    }

    // Get DtoProduct List With SubCategory or MainCategory
    public Page<DtoProduct> getAllProductWithParam(String mainCategory, String subCategory, Integer page) {
        Page<Product> products = filterProducts(mainCategory, subCategory, page);
        return products.map(productMapper::toDtoProduct);
    }
    private Page<Product> filterProducts(String mainCategory, String subCategory, Integer page) {
        int pageNumber = page != null ? page : 0;
        Pageable pageable = PageRequest.of(pageNumber, 10);
        Specification<Product> spec = Specification.where(null);
        // do nothing
        if (subCategory != null) {
            spec = spec.and(ProductSpecification.filterBySubCategory(subCategory));
        }
        if (mainCategory != null) {
            spec = spec.and(ProductSpecification.filterByMainCategory(mainCategory));
        }
        return productRepository.findAll(spec,pageable);
    }


    //Get DtoProduct List With Searching Keyword
    public Page<DtoProduct> getAllSearchingProductList(String searchingWord, Integer page) {
        Page<Product> productPage = findProductsByKeyword(searchingWord, page);
        return productPage.map(productMapper::toDtoProduct);
    }
    private Page<Product> findProductsByKeyword(String searchingWord, Integer page) {
        int pageNumber = page != null ? page : 0;
        Pageable pageable = PageRequest.of(pageNumber, 10);
        Specification<Product> spec = Specification.where(null);
        if(searchingWord != null && !searchingWord.isEmpty()){
            spec = spec
                    .or(ProductSpecification.nameContains(searchingWord))
                    .or(ProductSpecification.descriptionContains(searchingWord))
                    .or(ProductSpecification.mainCategoryContains(searchingWord))
                    .or(ProductSpecification.subCategoryContains(searchingWord));
        }

        return productRepository.findAll(spec, pageable);
    }


    // Get DtoProduct List With Price Range And Sorted
    public Page<DtoProduct> getAllProductsWithFilter(String mainCategory, String subCategory,  Double price_min, Double price_max, String sortString, Integer page){
        BigDecimal minPrice = price_min != null ? BigDecimal.valueOf(price_min) : BigDecimal.ZERO;
        BigDecimal maxPrice = price_max != null ? BigDecimal.valueOf(price_max) : BigDecimal.valueOf(Double.MAX_VALUE);
        Integer pageNumber = page != null ? page : 0;

        return findAllProductsWithPriceFilter(mainCategory,subCategory,minPrice,maxPrice,sortString,pageNumber);
    }
    private Page<DtoProduct> findAllProductsWithPriceFilter(String mainCategory, String subCategoryList, BigDecimal price_min, BigDecimal price_max, String sort, Integer page){
        Specification<Product> spec = Specification.where(null);
        if(mainCategory != null){
            spec = spec.and(ProductSpecification.mainCategoryIs(mainCategory));
        }
        if(subCategoryList != null){
            spec = spec.and(ProductSpecification.subCategoryIs(subCategoryList));
        }
        spec = spec.and(ProductSpecification.priceGreaterThenOrEqual(price_min));
        spec = spec.and(ProductSpecification.priceLessThenOrEqual(price_max));
        Pageable pageable = PageRequest.of(page, 10, createSort(sort));
        Page<Product> productPage = productRepository.findAll(spec,pageable);

        return productPage.map(productMapper::toDtoProduct);
    }
    private Sort createSort(String sortString){
        Sort sort;
        if(sortString.split("_")[1].equals("asc")){
            sort = Sort.by(Sort.Order.asc(sortString.split("_")[0]));
        } else if(sortString.split("_")[1].equals("desc")){
            sort = Sort.by(Sort.Order.desc(sortString.split("_")[0]));
        } else {
            throw new BaseException(new ErrorMessage(MessageType.GENERAL_EXCEPTION,""));
        }
        return sort;
    }



    // Get DtoProduct With id or Name
    public DtoProduct getProductWithParams(Long id, String name) {
        Specification<Product> spec = Specification.where(null);
        if (id != null && name == null) {
            spec = spec.and(ProductSpecification.hasId(id));
        } else if (name != null && id == null) {
           spec = spec.and(ProductSpecification.hasName(name));
        } else {
            throw new IllegalArgumentException("Either 'id' or 'name' must be provided, but not both.");
        }

        return productMapper.toDtoProduct(productRepository.findAll(spec).get(0)); // We assume that there is only one product with the given id or name. Because id and name are unique.
    }


    // Save New Product
    public DtoProduct saveProduct(DtoProductIU dtoProductIU) {
        Product newProduct = productMapper.toEntity(dtoProductIU);
        newProduct.setSubCategory(subCategoryRepository.findById(dtoProductIU.getSubCategory())
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,
                        "SubCategory with ID: " + dtoProductIU.getSubCategory()))));

        Product savedProduct = productRepository.save(newProduct);
        return productMapper.toDtoProduct(savedProduct);
    }


    // Update The Product
    public DtoProduct updateProduct(Long id, DtoProductIU dtoProductIU){
        Product updatedProduct = findProductById(id);
        updateProductFields(updatedProduct, dtoProductIU);
        updatedProduct.setSubCategory(findSubCategory(dtoProductIU.getSubCategory()));

        productRepository.save(updatedProduct);
        return productMapper.toDtoProduct(updatedProduct);
    }
    private Product findProductById(Long id) {
        return productRepository.getProductById(id)
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, id.toString())));
    }
    private SubCategory findSubCategory(Long subCategoryId) {
        return subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, "SubCategory with ID: " + subCategoryId)));
    }
    private void updateProductFields(Product product, DtoProductIU dtoProductIU) {
        product.setProductName(dtoProductIU.getProductName());
        product.setDescription(dtoProductIU.getDescription());
        product.setPrice(dtoProductIU.getPrice());
    }


    // Delete Product With id
    public String deleteProductById(Long id) {
        Product deletedProduct = findProductById(id);
        productRepository.delete(deletedProduct);
        return " product was deleted : " + id;
    }
}
