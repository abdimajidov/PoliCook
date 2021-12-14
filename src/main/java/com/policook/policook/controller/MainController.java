package com.policook.policook.controller;

import com.policook.policook.payload.ApiResponse;
import com.policook.policook.service.IngredientService;
import com.policook.policook.service.RecipeService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/main")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MainController {

    @Autowired
    RecipeService recipeService;

    @Autowired
    IngredientService ingredientService;

    @GetMapping("/get-recipe")
    public HttpEntity<?> getRecipes(){
        ApiResponse apiResponse=recipeService.get();
        return ResponseEntity.status(apiResponse.isSuccess()?200:400).body(apiResponse.getObject());
    }

    @GetMapping("/get-recipe/{id}")
    public HttpEntity<?> getRecipe(@PathVariable Long id){
        ApiResponse apiResponse=recipeService.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:400).body(apiResponse.getObject());
    }

    @GetMapping("/get-ingredient")
    public HttpEntity<?> getIngredients(){
        ApiResponse apiResponse=ingredientService.get();
        return ResponseEntity.status(apiResponse.isSuccess()?200:400).body(apiResponse.getObject());
    }

    @GetMapping("/get-ingredient/{id}")
    public HttpEntity<?> getIngredient(@PathVariable Long id){
        ApiResponse apiResponse=ingredientService.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:400).body(apiResponse.getObject());
    }

    @GetMapping("/search-ingredient")
    public HttpEntity<?> searchIngredient(@RequestParam String name){
        ApiResponse apiResponse=ingredientService.getName(name);
        return ResponseEntity.status(apiResponse.isSuccess()?200:400).body(apiResponse.getObject());
    }

    @GetMapping("/search-recipe")
    public HttpEntity<?> searchRecipe(@RequestParam String name){
        ApiResponse apiResponse=recipeService.getName(name);
        return ResponseEntity.status(apiResponse.isSuccess()?200:400).body(apiResponse.getObject());
    }
}
