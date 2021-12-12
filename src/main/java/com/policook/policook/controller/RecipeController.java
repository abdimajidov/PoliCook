package com.policook.policook.controller;

import com.policook.policook.payload.ApiResponse;
import com.policook.policook.service.RecipeService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/recipe")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecipeController {



    @Autowired
    RecipeService recipeService;

    @GetMapping
    public HttpEntity<?> getRecipe(){
        ApiResponse apiResponse=recipeService.get();
        return ResponseEntity.status(apiResponse.isSuccess()?200:400).body(apiResponse.getObject());
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getRecipeById(@PathVariable Long id){
        ApiResponse apiResponse=recipeService.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:400).body(apiResponse.getObject());
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteRecipe(@PathVariable Long id){
        ApiResponse apiResponse=recipeService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:400).body(apiResponse.getObject());
    }

    @PostMapping
    public HttpEntity<?> addRecipe(@RequestPart("name") String name,
                                       @RequestPart("photo") MultipartFile file ){
        ApiResponse apiResponse=recipeService.addRecipe(name,file);
        return ResponseEntity.status(apiResponse.isSuccess()?200:400).body(apiResponse.getObject());
    }

    @PostMapping("/add-ingredient")
    public HttpEntity<?> addIngredientToRecipe(@RequestParam("recipe") Long recipeId,
                                               @RequestParam("ingredient") List<Long> ingredientIdList){
        ApiResponse apiResponse=recipeService.addIngredientToRecipe(recipeId,ingredientIdList);
        return ResponseEntity.status(apiResponse.isSuccess()?200:400).body(apiResponse.getObject());
    }
}
