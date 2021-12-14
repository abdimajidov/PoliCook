package com.policook.policook.payload;

import com.policook.policook.entity.Role;
import lombok.Data;

@Data
public class UserDto {
    String name;

    String surName;

    String email;

    String password;

    Role role;

    String birthDate;
}
