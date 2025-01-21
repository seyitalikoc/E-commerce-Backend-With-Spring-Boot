package com.seyitkoc.service;

import com.seyitkoc.dto.DtoUser;
import com.seyitkoc.dto.DtoUserIU;
import com.seyitkoc.dto.UpdatePassword;
import com.seyitkoc.entity.Cart;
import com.seyitkoc.entity.User;
import com.seyitkoc.exception.BaseException;
import com.seyitkoc.exception.ErrorMessage;
import com.seyitkoc.exception.MessageType;
import com.seyitkoc.mapper.UserMapper;
import com.seyitkoc.repository.CartRepository;
import com.seyitkoc.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, CartRepository cartRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.userMapper = userMapper;
    }

    // Get User From id or Email
    public DtoUser getUserWithParams(Long id, String email){
        User user;
        if (id != null && email == null){
            user = findUserByUserId(id);
        } else if(email != null && id == null){
            user = findUserByEmail(email);
        } else {
            throw new IllegalArgumentException("Either 'id' or 'email' must be provided, but not both.");
        }

        return userMapper.toDtoUser(user);
    }
    private User findUserByUserId(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, id.toString())));
    }
    private User findUserByEmail(String email){
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,email)));
    }


    // Save User
    public DtoUser saveUser(DtoUserIU dtoUserIU) {
        if(findUserByEmail(dtoUserIU.getEmail())!= null){
            throw new BaseException(new ErrorMessage(MessageType.GENERAL_EXCEPTION,"Email already used."));
        }
        User newUser = createUser(dtoUserIU);
        Cart cart = createCartForUser(newUser);
        newUser.setCart(cart);
        userRepository.save(newUser);
        return userMapper.toDtoUser(newUser);
    }
    private User createUser(DtoUserIU dtoUserIU){
        User user = userMapper.toEntity(dtoUserIU);
        return userRepository.save(user);
    }
    private Cart createCartForUser(User newUser) {
        Cart cart = new Cart();
        cart.setUser(newUser);
        cart.setProductList(new ArrayList<>());
        cartRepository.save(cart);
        return cart;
    }


    // Update User By Email
    public DtoUser updateUserInfos(String email, DtoUserIU dtoUserIU){
        User user = findUserByEmail(email);
        updateUserDetails(user, dtoUserIU);
        if (isPasswordChanged(user.getPassword(), dtoUserIU.getPassword())) {
            user.setPassword(encodePassword(dtoUserIU.getPassword()));
        }
        userRepository.save(user);
        return userMapper.toDtoUser(user);
    }
    private void updateUserDetails(User user, DtoUserIU dtoUserIU) {
        user.setFirstName(dtoUserIU.getFirstName());
        user.setLastName(dtoUserIU.getLastName());
        user.setEmail(dtoUserIU.getEmail());
        user.setCart(user.getCart());
    }
    private boolean isPasswordChanged(String existingPassword, String newPassword) {
        return !existingPassword.equals(new BCryptPasswordEncoder().encode(newPassword));  // The password not match
    }
    private String encodePassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }


    public DtoUser updateUserPassword(String email, UpdatePassword updatePassword){
        User user = findUserByEmail(email);
        if (passwordCheck(user.getPassword(),updatePassword)){
            throw new BaseException(new ErrorMessage(MessageType.GENERAL_EXCEPTION,"Old password is incorrect."));
        }
        user.setPassword(new BCryptPasswordEncoder().encode(updatePassword.getNewPassword()));
        userRepository.save(user);

        return userMapper.toDtoUser(user);
    }
    private boolean passwordCheck(String oldPassword, UpdatePassword updatePassword){
        return isPasswordChanged(oldPassword, updatePassword.getOldPassword());
    }

    // Delete User By id
    public String deleteUser(Long id) {
        User user = findUserByUserId(id);

        userRepository.delete(user);
        return "User deleted: " + id;
    }
}
