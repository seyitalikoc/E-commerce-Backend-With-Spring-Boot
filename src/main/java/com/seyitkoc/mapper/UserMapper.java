package com.seyitkoc.mapper;

import com.seyitkoc.dto.DtoCart;
import com.seyitkoc.dto.DtoProduct;
import com.seyitkoc.dto.DtoUser;
import com.seyitkoc.dto.DtoUserIU;
import com.seyitkoc.entity.Cart;
import com.seyitkoc.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final ProductMapper productMapper;

    public UserMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public DtoUser toDtoUser(User user) {
        DtoUser dtoUser = new DtoUser();
        DtoCart dtoCart = new DtoCart();
        BeanUtils.copyProperties(user,dtoUser);
        BeanUtils.copyProperties(user.getCart(),dtoCart);

        List<DtoProduct> dtoProductList = user.getCart().getProductList().stream()
                .map(productMapper::toDtoProduct)
                .collect(Collectors.toList());
        dtoCart.setProductList(dtoProductList);
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
