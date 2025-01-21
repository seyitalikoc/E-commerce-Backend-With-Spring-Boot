package com.seyitkoc.service;

import com.seyitkoc.dto.DtoProduct;
import com.seyitkoc.dto.DtoProductIU;
import com.seyitkoc.entity.MainCategory;
import com.seyitkoc.entity.Product;
import com.seyitkoc.entity.SubCategory;
import com.seyitkoc.exception.BaseException;
import com.seyitkoc.exception.ErrorMessage;
import com.seyitkoc.exception.MessageType;
import com.seyitkoc.mapper.ProductMapper;
import com.seyitkoc.repository.MainCategoryRepository;
import com.seyitkoc.repository.ProductRepository;
import com.seyitkoc.repository.SubCategoryRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final MainCategoryRepository mainCategoryRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository,
                          SubCategoryRepository subCategoryRepository,
                          MainCategoryRepository mainCategoryRepository,
                          ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.mainCategoryRepository = mainCategoryRepository;
        this.productMapper = productMapper;
    }

    // Get DtoProduct List With SubCategory or MainCategory
    public List<DtoProduct> getAllProductWithParam(String mainCategory, String subCategory) {
        List<Product> products = filterProducts(mainCategory, subCategory);
        return productMapper.toDtoProducts(products);
    }
    private List<Product> filterProducts(String mainCategory, String subCategory) {

        if (mainCategory == null && subCategory == null) {
            return productRepository.findAll();
        }

        if (mainCategory == null) {
            return filterBySubCategory(subCategory);
        }

        if (subCategory == null) {
            return filterByMainCategory(mainCategory);
        }

        return new ArrayList<>();
    }
    private List<Product> filterBySubCategory(String subCategory) {
        return subCategoryRepository.getSubCategoryByCategoryName(subCategory)
                .map(productRepository::findAllBySubCategory)
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, subCategory)));
    }
    private List<Product> filterByMainCategory(String mainCategory) {
        return mainCategoryRepository.getMainCategoryByCategoryName(mainCategory)
                .map(mainCat -> mainCat.getSubCategories()
                        .stream()
                        .flatMap(subCat -> subCat.getProducts().stream())
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, mainCategory)));
    }


    //Get DtoProduct List With Searching Keyword
    public List<DtoProduct> getAllSearchingProductList(String searchingWord) {
        List<Product> productList = findProductsByKeyword(searchingWord);
        return productMapper.toDtoProducts(productList);
    }
    private List<Product> findProductsByKeyword(String searchingWord) {

        // Search keyword in product's name or description
        List<Product> productList = new ArrayList<>(productRepository.findByDescriptionOrProductNameContaining(searchingWord));

        // Search keyword on SubCategories
        List<SubCategory> subCategoryList = subCategoryRepository.findByKeyword(searchingWord);
        if (subCategoryList != null) {
            for (SubCategory subCategory : subCategoryList) {
                productList.addAll(productRepository.getProductsBySubCategory(subCategory));
            }
        }

        // Search on MainCategories
        List<MainCategory> mainCategoryList = mainCategoryRepository.findByKeyword(searchingWord);
        if (mainCategoryList != null) {
            for (MainCategory mainCategory : mainCategoryList) {
                for (SubCategory subCategory : mainCategory.getSubCategories()) {
                    productList.addAll(productRepository.getProductsBySubCategory(subCategory));
                }
            }
        }

        return productList;
    }


    // Get DtoProduct List With Price Range And Sorted
    public List<DtoProduct> getAllProductsWithFilter(String mainCategory, String subCategory,  Double price_min, Double price_max, String sortString){
        List<SubCategory> subCategoryList = new ArrayList<>();
        if (mainCategory == null && subCategory != null){
            subCategoryList.add(getSubCategoryByName(subCategory));
        } else if (subCategory == null &&  mainCategory != null){
            subCategoryList.addAll(getSubCategoriesFromMainCategoryName(mainCategory));
        }
        return productMapper.toDtoProducts(findAllProductsWithPriceFilter(subCategoryList,price_min,price_max,sortString));
    }
    private void sortList(String sortString, List<Product> productList){

        if(sortString.split("_")[1].equals("asc")){
            productList.sort(Comparator.comparing(Product::getPrice));
        } else if(sortString.split("_")[1].equals("desc")){
            productList.sort(Comparator.comparing(Product::getPrice).reversed());
        } else {
            throw new BaseException(new ErrorMessage(MessageType.GENERAL_EXCEPTION,""));
        }
    }
    private List<Product> findAllProductsWithPriceFilter(List<SubCategory> subCategoryList, Double price_min, Double price_max, String sort){
        List<Product> productList = new ArrayList<>();

        BigDecimal minPrice = price_min != null ? BigDecimal.valueOf(price_min) : BigDecimal.ZERO;
        BigDecimal maxPrice = price_max != null ? BigDecimal.valueOf(price_max) : BigDecimal.valueOf(Double.MAX_VALUE);

        for (SubCategory subCategory : subCategoryList){
            productList.addAll(productRepository.findProductsByFilter(subCategory, minPrice, maxPrice));
        }

        sortList(sort, productList);
        return productList;
    }
    private SubCategory getSubCategoryByName(String name){
        return subCategoryRepository.findSubCategoryByCategoryName(name)
                .orElseThrow(()->new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,name)));
    }
    private List<SubCategory> getSubCategoriesFromMainCategoryName(String name){
         MainCategory mainCategory= mainCategoryRepository.findMainCategoryByCategoryName(name)
                 .orElseThrow(()-> new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,name)));

         return mainCategory.getSubCategories();
    }


    // Get DtoProduct With id or Name
    public DtoProduct getProductWithParams(Long id, String name) {
        Product product;
        if (id != null && name == null) {
            product = findProductById(id);
        } else if (name != null && id == null) {
            product = findProductByName(name);
        } else {
            throw new IllegalArgumentException("Either 'id' or 'name' must be provided, but not both.");
        }

        return productMapper.toDtoProduct(product);
    }
    private Product findProductById(Long id) {
        return productRepository.getProductById(id)
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, id.toString())));
    }
    private Product findProductByName(String name) {
        return productRepository.getProductByProductName(name)
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, name)));
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
