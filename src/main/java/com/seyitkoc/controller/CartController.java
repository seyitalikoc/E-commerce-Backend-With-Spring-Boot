package com.seyitkoc.controller;

import com.seyitkoc.dto.DtoCart;
import com.seyitkoc.entity.RootEntity;
import com.seyitkoc.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PutMapping("/addToCart")
    public RootEntity<DtoCart> addItemToCart(@RequestParam(value = "itemId") Long itemId, @RequestParam(value = "userEmail") String email){
        return RootEntity.ok(cartService.addItemToCart(itemId, email));
    }

    @PutMapping("/removeFromCart")
    public RootEntity<DtoCart> removeItemFromCart(@RequestParam(value = "itemId") Long itemId, @RequestParam(value = "userEmail") String email){
        return RootEntity.ok(cartService.removeItemFromCart(itemId, email));
    }

}
