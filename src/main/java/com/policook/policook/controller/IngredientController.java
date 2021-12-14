package com.policook.policook.controller;

import com.policook.policook.payload.ApiResponse;
import com.policook.policook.service.IngredientService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ingredient")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IngredientController {
    @Autowired
    IngredientService ingredientService;

    @PreAuthorize(value = "hasAuthority('USER') or hasAuthority('ADMIN')")
    @GetMapping
    public HttpEntity<?> getIngredient(){
        ApiResponse apiResponse=ingredientService.get();
        return ResponseEntity.status(apiResponse.isSuccess()?200:400).body(apiResponse.getObject());
    }

    @PreAuthorize(value = "hasAuthority('USER') or hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public HttpEntity<?> getIngredientById(@PathVariable Long id){
        ApiResponse apiResponse=ingredientService.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:400).body(apiResponse.getObject());
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteIngredient(@PathVariable Long id){
        ApiResponse apiResponse=ingredientService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:400).body(apiResponse.getObject());
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PostMapping
    public HttpEntity<?> addIngredient(@RequestPart("name") String name,
                                       @RequestPart("photo") MultipartFile file ){
        ApiResponse apiResponse=ingredientService.addIngredient(name,file);
        return ResponseEntity.status(apiResponse.isSuccess()?200:400).body(apiResponse.getObject());
    }
}
