package com.seyitkoc.mapper;

import com.seyitkoc.dto.*;
import com.seyitkoc.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final CartItemMapper cartItemMapper;

    public UserMapper(CartItemMapper cartItemMapper) {
        this.cartItemMapper = cartItemMapper;
    }

    public DtoUser toDtoUser(User user) {
        DtoUser dtoUser = new DtoUser();
        DtoCart dtoCart = new DtoCart();
        BeanUtils.copyProperties(user,dtoUser);
        BeanUtils.copyProperties(user.getCart(),dtoCart);

        List<DtoCartItem> dtoCartItems = user.getCart().getCartItems().stream()
                .map(cartItemMapper::toDtoCartItem)
                .collect(Collectors.toList());
        dtoCart.setCartItems(dtoCartItems);
        dtoUser.setCart(dtoCart);
        return dtoUser;
    }

    public User toEntity(DtoUserIU dtoUserIU){
        User user = new User();
        user.setFirstName(dtoUserIU.getFirstName());
        user.setLastName(dtoUserIU.getLastName());
        user.setEmail(dtoUserIU.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(dtoUserIU.getPassword()));
        return user;
    }
}
