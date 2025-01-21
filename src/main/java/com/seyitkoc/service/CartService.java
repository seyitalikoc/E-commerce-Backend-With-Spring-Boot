package com.seyitkoc.service;

import com.seyitkoc.dto.DtoCart;
import com.seyitkoc.entity.Cart;
import com.seyitkoc.entity.Product;
import com.seyitkoc.entity.User;
import com.seyitkoc.exception.BaseException;
import com.seyitkoc.exception.ErrorMessage;
import com.seyitkoc.exception.MessageType;
import com.seyitkoc.mapper.CartMapper;
import com.seyitkoc.repository.CartRepository;
import com.seyitkoc.repository.ProductRepository;
import com.seyitkoc.repository.UserRepository;
import org.springframework.stereotype.Service;



@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    public CartService(CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartMapper = cartMapper;
    }

    // Add Product To Cart
    public DtoCart addItemToCart(Long itemId, String email) {
        User user = findUserByEmail(email);
        Cart cart = user.getCart();
        Product product = findProductById(itemId);

        if (cartContainsProduct(cart, product)) {
            throw new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, "Product already in cart"));
        }
        addProductToCart(cart, product);
        cartRepository.save(cart);

        return cartMapper.toDtoCart(cart);
    }
    private User findUserByEmail(String email){
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,email)));
    }
    private Product findProductById(Long itemId) {
        return productRepository.getProductById(itemId)
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, itemId.toString())));
    }
    private boolean cartContainsProduct(Cart cart, Product product) {
        return cart.getProductList().contains(product);
    }
    private void addProductToCart(Cart cart, Product product) {
        cart.getProductList().add(product);
    }


    // Remove Product From Cart
    public DtoCart removeItemFromCart(Long itemId, String email) {
        User user = findUserByEmail(email);
        Cart cart = user.getCart();
        Product product = findProductById(itemId);
        if (!cartContainsProduct(cart,product)){
            throw new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, "Product already in cart"));
        }
        removeProductFromCart(cart,product);
        cartRepository.save(cart);

        return cartMapper.toDtoCart(cart);
    }
    private void removeProductFromCart(Cart cart, Product product){
        cart.getProductList().remove(product);
    }

}
