package com.seyitkoc.controller;

import com.seyitkoc.dto.DtoUser;
import com.seyitkoc.dto.DtoUserIU;
import com.seyitkoc.dto.UpdatePassword;
import com.seyitkoc.entity.RootEntity;
import com.seyitkoc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public RootEntity<DtoUser> getUserWithParams(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "email", required = false) String email){
        return RootEntity.ok(userService.getUserWithParams(id, email));
    }

    @PostMapping("/save")
    public RootEntity<DtoUser> saveUser(@RequestBody DtoUserIU dtoUserIU) {
        return RootEntity.ok(userService.saveUser(dtoUserIU));
    }

    @PutMapping("/update")
    public RootEntity<DtoUser> updateUserInfos(@RequestParam(value = "email") String email, @RequestBody DtoUserIU dtoUserIU){
        return RootEntity.ok(userService.updateUserInfos(email, dtoUserIU));
    }

    @PutMapping("/passwordUpdate")
    public RootEntity<DtoUser> updateUserPassword(@RequestParam(value = "email") String email, @RequestBody UpdatePassword updatePassword){
        return RootEntity.ok(userService.updateUserPassword(email,updatePassword));
    }

    @DeleteMapping("/delete")
    public RootEntity<String> deleteUser(@RequestParam(value = "id") Long id){
        return RootEntity.ok(userService.deleteUser(id));
    }
}