package com.seyitkoc.mapper;

import com.seyitkoc.dto.DtoCart;
import com.seyitkoc.dto.DtoCartItem;
import com.seyitkoc.entity.Cart;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartMapper {

    private final CartItemMapper cartItemMapper;

    public CartMapper(CartItemMapper cartItemMapper) {
        this.cartItemMapper = cartItemMapper;
    }

    public DtoCart toDtoCart(Cart cart){
        DtoCart dtoCart = new DtoCart();

        List<DtoCartItem> cartItemList = cart.getCartItems().stream()
                .map(cartItemMapper::toDtoCartItem)
                .toList();

        dtoCart.setCartItems(cartItemList);
        return dtoCart;
    }
}
