package com.policook.policook.controller;

import com.policook.policook.payload.ApiResponse;
import com.policook.policook.payload.UserDto;
import com.policook.policook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @GetMapping()
    public HttpEntity<?> getUserById(@RequestParam Long userId){
        ApiResponse apiResponse=userService.getById(userId);
        return ResponseEntity.status(apiResponse.isSuccess()?200:400).body(apiResponse.getObject());
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PostMapping()
    public HttpEntity<?> addUser(@RequestBody UserDto userDto){
        ApiResponse apiResponse=userService.addUser(userDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:400).body(apiResponse.getObject());
    }
}
