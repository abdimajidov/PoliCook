package com.policook.policook.service;

import com.policook.policook.payload.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecipeService {
    ApiResponse get();

    ApiResponse delete(Long id);

    ApiResponse addRecipe(String name, MultipartFile file);

    ApiResponse getById(Long id);

    ApiResponse addIngredientToRecipe(Long recipeId, List<Long> ingredientIdList);

    ApiResponse getName(String name);

}
