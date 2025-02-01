package com.seyitkoc.service;

import com.seyitkoc.dto.DtoProduct;
import com.seyitkoc.dto.DtoProductIU;
import com.seyitkoc.entity.Product;
import com.seyitkoc.entity.ProductCategory;
import com.seyitkoc.exception.BaseException;
import com.seyitkoc.exception.ErrorMessage;
import com.seyitkoc.exception.MessageType;
import com.seyitkoc.mapper.ProductMapper;
import com.seyitkoc.repository.ProductCategoryRepository;
import com.seyitkoc.repository.ProductRepository;
import com.seyitkoc.specification.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductCategoryRepository productCategoryRepository;

    public ProductService(ProductRepository productRepository,
                          ProductMapper productMapper, ProductCategoryRepository productCategoryRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productCategoryRepository = productCategoryRepository;
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
                    .or(ProductSpecification.categoryContains(searchingWord));
        }

        return productRepository.findAll(spec, pageable);
    }


    // Get DtoProduct List With Price Range And Sorted
    public Page<DtoProduct> getAllProductsWithFilter(String category, Double price_min, Double price_max, String sortString, Integer page){
        BigDecimal minPrice = price_min != null ? BigDecimal.valueOf(price_min) : BigDecimal.ZERO;
        BigDecimal maxPrice = price_max != null ? BigDecimal.valueOf(price_max) : BigDecimal.valueOf(Double.MAX_VALUE);
        Integer pageNumber = page != null ? page : 0;

        return findAllProductsWithPriceFilter(category,minPrice,maxPrice,sortString,pageNumber);
    }
    private Page<DtoProduct> findAllProductsWithPriceFilter(String category, BigDecimal price_min, BigDecimal price_max, String sort, Integer page){
        Specification<Product> spec = buildProductSpecification(category, price_min, price_max);
        Pageable pageable = PageRequest.of(page, 10, createSort(sort));
        Page<Product> productPage = productRepository.findAll(spec,pageable);

        return productPage.map(productMapper::toDtoProduct);
    }
    private Specification<Product> buildProductSpecification(String categorySlug, BigDecimal price_min, BigDecimal price_max){
        Specification<Product> spec = Specification.where(null);
        if(categorySlug != null && !categorySlug.isEmpty()){
            spec = spec.and(buildCategorySpecification(categorySlug));
        }
        spec = spec.and(ProductSpecification.priceGreaterThenOrEqual(price_min));
        spec = spec.and(ProductSpecification.priceLessThenOrEqual(price_max));
        return spec;
    }
    private Specification<Product> buildCategorySpecification(String categorySlug){
        return productCategoryRepository.findProductCategoryBySlug(categorySlug)
                .map(category -> {
                    if (!category.getSubCategories().isEmpty()) {
                        Specification<Product> parentSpec = ProductSpecification.categoryContains(categorySlug);
                        Specification<Product> childrenSpec = category.getSubCategories().stream()
                                .map(subCategory -> ProductSpecification.categoryContains(subCategory.getSlug()))
                                .reduce(Specification.where(null), Specification::or);

                        return parentSpec.or(childrenSpec);
                    } else {
                        return ProductSpecification.categoryContains(categorySlug);
                    }
                })
                .orElse(Specification.where(null));
    }
    private Sort createSort(String sortString){
        Sort sort = Sort.unsorted();
        if(sortString == null || sortString.isEmpty()){
            return sort;
        }
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
        newProduct.setCategory(productCategoryRepository.findById(dtoProductIU.getCategoryId())
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,
                        "SubCategory with ID: " + dtoProductIU.getCategoryId()))));

        Product savedProduct = productRepository.save(newProduct);
        return productMapper.toDtoProduct(savedProduct);
    }


    // Update The Product
    public DtoProduct updateProduct(Long id, DtoProductIU dtoProductIU){
        Product updatedProduct = findProductById(id);
        updateProductFields(updatedProduct, dtoProductIU);
        updatedProduct.setCategory(findCategory(dtoProductIU.getCategoryId()));

        productRepository.save(updatedProduct);
        return productMapper.toDtoProduct(updatedProduct);
    }
    private Product findProductById(Long id) {
        return productRepository.getProductById(id)
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, id.toString())));
    }
    private ProductCategory findCategory(Long categoryId) {
        return productCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, "SubCategory with ID: " + categoryId)));
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
