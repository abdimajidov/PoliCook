package com.policook.policook.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse {

    String message;
    boolean success;
    Object object;

    public ApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}