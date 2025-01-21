package com.seyitkoc.controller;

import com.seyitkoc.dto.DtoLoginRequest;
import com.seyitkoc.dto.DtoUser;
import com.seyitkoc.entity.RootEntity;
import com.seyitkoc.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public RootEntity<DtoUser> login(@RequestBody DtoLoginRequest dtoLoginRequest){

        return RootEntity.ok(authService.login(dtoLoginRequest));
    }

}
