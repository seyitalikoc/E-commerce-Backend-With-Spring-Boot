package com.seyitkoc.mapper;

import com.seyitkoc.dto.DtoCartItem;
import com.seyitkoc.entity.CartItem;
import com.seyitkoc.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapper {

    private final ProductMapper productMapper;

    public CartItemMapper (ProductMapper productMapper){
        this.productMapper = productMapper;
    }

    public DtoCartItem toDtoCartItem(CartItem cartItem){
        DtoCartItem dtoCartItem = new DtoCartItem();
        dtoCartItem.setProduct(productMapper.toDtoProduct(cartItem.getProduct()));
        dtoCartItem.setQuantity(cartItem.getQuantity());
        return dtoCartItem;
    }

    public CartItem toCartItem(Product product) {
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(1);
        return cartItem;
    }
}
