package com.policook.policook.dataLoader;


import com.policook.policook.entity.*;
import com.policook.policook.repository.IngredientRepository;
import com.policook.policook.repository.RecipeIngredientRepository;
import com.policook.policook.repository.RecipeRepository;
import com.policook.policook.repository.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataLoader implements CommandLineRunner {

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String mode;

    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RecipeIngredientRepository recipeIngredientRepository;

    @Override
    public void run(String... args) {
        if (mode.equals("create")) {

            User user=new User();
            user.setName("user");
            user.setSurName("userSurname");
            user.setEmail("user@Email");
            user.setRoles(Collections.singleton(Role.USER));
            user.setPassword(passwordEncoder.encode("user123"));
            userRepository.save(user);

            User user2=new User();
            user2.setName("admin");
            user2.setSurName("adminSurname");
            user2.setEmail("admin@Email");
            user2.setRoles(Collections.singleton(Role.ADMIN));
            user2.setPassword(passwordEncoder.encode("admin123"));
            userRepository.save(user2);

            int j=1,k=1;
            for (int i=1;i<=20;i++){
                Ingredient ingredient=new Ingredient("Ingredient № "+i);
                Ingredient savedIngredient = ingredientRepository.save(ingredient);

                if(i%4==1) {
                    Recipe recipe = new Recipe("Recipe № " + j);
                    Recipe savedRecipe = recipeRepository.save(recipe);
                    RecipeIngredient recipeIngredient = new RecipeIngredient(savedRecipe,savedIngredient);
                    recipeIngredientRepository.save(recipeIngredient);
                    j++;
                }
            }

        }
    }
}
