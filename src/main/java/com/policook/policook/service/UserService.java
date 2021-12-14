package com.policook.policook.service;

import com.policook.policook.payload.ApiResponse;
import com.policook.policook.payload.UserDto;

public interface UserService {

    ApiResponse getById(Long userId);

    ApiResponse addUser(UserDto userDto);
}
