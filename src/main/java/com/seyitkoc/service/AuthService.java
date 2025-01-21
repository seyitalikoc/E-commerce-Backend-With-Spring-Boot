package com.seyitkoc.service;

import com.seyitkoc.dto.DtoLoginRequest;
import com.seyitkoc.dto.DtoUser;
import com.seyitkoc.entity.User;
import com.seyitkoc.exception.BaseException;
import com.seyitkoc.exception.ErrorMessage;
import com.seyitkoc.exception.MessageType;
import com.seyitkoc.mapper.UserMapper;
import com.seyitkoc.repository.UserRepository;
import com.seyitkoc.security.jwt.JwtTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public AuthService(UserRepository userRepository, JwtTokenService jwtTokenService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.jwtTokenService = jwtTokenService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    // User Login
    public DtoUser login(DtoLoginRequest dtoLoginRequest) {
        User user = findUserByEmail(dtoLoginRequest.getEmail());
        if (!passwordEncoder.matches(dtoLoginRequest.getPassword(), user.getPassword())) {
            throw new BaseException(new ErrorMessage(MessageType.INVALID_CREDENTIALS, ""));
        }

        DtoUser dtoUser = userMapper.toDtoUser(user);
        dtoUser.setToken(authenticate(dtoLoginRequest));

        return dtoUser;
    }
    private User findUserByEmail(String email){
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,email)));
    }
    // Authenticate Login Request
    private String authenticate(DtoLoginRequest dtoLoginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dtoLoginRequest.getEmail(), dtoLoginRequest.getPassword()));

        } catch (BadCredentialsException e) {
            throw new BaseException(new ErrorMessage(MessageType.INVALID_CREDENTIALS, "Invalid email or password."));
        } catch (Exception e) {
            throw new BaseException(new ErrorMessage(MessageType.AUTHENTICATION_FAILED, "Authentication failed."));
        }
        User user = findUserByEmail(dtoLoginRequest.getEmail());

        return jwtTokenService.generateToken(user);
    }


}
