package com.seyitkoc.service;

import com.seyitkoc.dto.DtoCart;
import com.seyitkoc.entity.Cart;
import com.seyitkoc.entity.CartItem;
import com.seyitkoc.entity.Product;
import com.seyitkoc.entity.User;
import com.seyitkoc.exception.BaseException;
import com.seyitkoc.exception.ErrorMessage;
import com.seyitkoc.exception.MessageType;
import com.seyitkoc.mapper.CartItemMapper;
import com.seyitkoc.mapper.CartMapper;
import com.seyitkoc.repository.CartItemRepository;
import com.seyitkoc.repository.CartRepository;
import com.seyitkoc.repository.ProductRepository;
import com.seyitkoc.repository.UserRepository;
import org.springframework.stereotype.Service;



@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;

    public CartService(CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository,
                       CartItemRepository cartItemRepository, CartMapper cartMapper, CartItemMapper cartItemMapper) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartMapper = cartMapper;
        this.cartItemMapper = cartItemMapper;
    }

    // Add Product To Cart
    public DtoCart addItemToCart(Long itemId, String email) {
        User user = findUserByEmail(email);
        Cart cart = user.getCart();
        Product product = findProductById(itemId);

        if (cartContainsProduct(cart, product)) {
            increaseQuantity(cart, product);
        }
        else{
            addProductToCart(cart, product);
            cartRepository.save(cart);
        }
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
        return cart.getCartItems().stream()
                .anyMatch(cartItem -> cartItem.getProduct().equals(product));
    }
    private void addProductToCart(Cart cart, Product product) {
        CartItem cartItem = generateCartItems(cart, product);
        cart.getCartItems().add(cartItem);
    }
    private CartItem generateCartItems(Cart cart, Product product){
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(1);
        cartItem.setCart(cart);
        return cartItemRepository.save(cartItem);
    }


    // Remove Product From Cart
    public DtoCart removeItemFromCart(Long itemId, String email) {
        User user = findUserByEmail(email);
        Cart cart = user.getCart();
        Product product = findProductById(itemId);
        removeProductFromCart(cart,product);
        cartRepository.save(cart);

        return cartMapper.toDtoCart(cart);
    }
    private void removeProductFromCart(Cart cart, Product product){
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst()
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, "Product not in cart")));
        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
    }


    // Increase Product Quantity
    public DtoCart decreaseItemFromCart(Long itemId, String email) {
        User user = findUserByEmail(email);
        Cart cart = user.getCart();
        Product product = findProductById(itemId);
        if (!cartContainsProduct(cart,product)){
            throw new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, "Product not in cart"));
        }
        decreaseQuantity(cart,product);
        cartRepository.save(cart);

        return cartMapper.toDtoCart(cart);
    }
    private void decreaseQuantity(Cart cart, Product product){
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst()
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, "Product not in cart")));
        if (cartItem.getQuantity() == 1){
            cart.getCartItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            cartItemRepository.save(cartItem);
        }
    }


    // Decrease Product Quantity
    public DtoCart increaseItemFromCart(Long itemId, String email) {
        User user = findUserByEmail(email);
        Cart cart = user.getCart();
        Product product = findProductById(itemId);
        if (!cartContainsProduct(cart,product)){
            throw new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, "Product not in cart"));
        }
        increaseQuantity(cart,product);
        cartRepository.save(cart);

        return cartMapper.toDtoCart(cart);
    }
    private void increaseQuantity(Cart cart, Product product){
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst()
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, "Product not in cart")));
        cartItem.setQuantity(cartItem.getQuantity() + 1);
        cartItemRepository.save(cartItem);
    }
}
