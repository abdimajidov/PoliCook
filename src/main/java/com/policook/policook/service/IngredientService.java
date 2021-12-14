package com.policook.policook.service;

import com.policook.policook.payload.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IngredientService {
    ApiResponse get();

    ApiResponse delete(Long id);

    ApiResponse addIngredient(String name, MultipartFile file);

    ApiResponse getById(Long id);

    ApiResponse getName(String name);
}
