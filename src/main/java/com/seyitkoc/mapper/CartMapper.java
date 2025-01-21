package com.seyitkoc.mapper;

import com.seyitkoc.dto.DtoCart;
import com.seyitkoc.dto.DtoProduct;
import com.seyitkoc.entity.Cart;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartMapper {

    private final ProductMapper productMapper;

    public CartMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public DtoCart toDtoCart(Cart cart){
        DtoCart dtoCart = new DtoCart();

        List<DtoProduct> dtoProductList = cart.getProductList().stream()
                .map(productMapper::toDtoProduct)
                .toList();

        dtoCart.setProductList(dtoProductList);
        return dtoCart;
    }
}
