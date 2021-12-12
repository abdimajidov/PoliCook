package com.policook.policook.serviceImpl;

import com.policook.policook.entity.Ingredient;
import com.policook.policook.entity.Photo;
import com.policook.policook.entity.Recipe;
import com.policook.policook.entity.RecipeIngredient;
import com.policook.policook.payload.ApiResponse;
import com.policook.policook.repository.IngredientRepository;
import com.policook.policook.repository.PhotoRepository;
import com.policook.policook.repository.RecipeIngredientRepository;
import com.policook.policook.repository.RecipeRepository;
import com.policook.policook.service.RecipeService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
public class RecipeServiceImpl implements RecipeService {
    @Value("${upload.recipes.path}")
    String uploadPath;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    PhotoRepository photoRepository;

    @Autowired
    IngredientRepository ingredientRepository;

    @Autowired
    RecipeIngredientRepository recipeIngredientRepository;

    @Override
    public ApiResponse get() {
        if (recipeRepository.findAll().size() != 0) {
            List<Recipe> recipeList = recipeRepository.findAll();

            List<List<Object>> mainList=new ArrayList<>();

            for (Recipe recipe : recipeList) {
                Map<String,Recipe> recipeMap=new HashMap<>();
                Map<String,List<Ingredient>> ingredientMap=new HashMap<>();
                List<Object> attachmentList=new ArrayList<>();
                List<Ingredient> ingredientList=new ArrayList<>();
                recipeMap.put("Recipe:",recipe);
                List<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findByRecipeId(recipe.getId());
                for (RecipeIngredient recipeIngredient : recipeIngredients) {
                    ingredientList.add(recipeIngredient.getIngredient());
                }
                ingredientMap.put("Ingredient:",ingredientList);
                attachmentList.add(recipeMap);
                attachmentList.add(ingredientMap);
                mainList.add(attachmentList);

            }


            return new ApiResponse("Recipe List:", true, mainList);
        }
        else return new ApiResponse("List is empty", false, "List is Empty");
    }

    @Override
    public ApiResponse delete(Long id) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        if (recipeOptional.isEmpty()) return new ApiResponse("Recipe not found", false, "Recipe not found");

        String photo = recipeOptional.get().getPhoto().getName();
        File file = new File(uploadPath + "/" + photo);
        if (!file.delete()) {
            return new ApiResponse("Photo don;t delete", false, "Photo don't delete");
        }
        recipeRepository.deleteById(id);
        return new ApiResponse("Recipe deleted", true, "id=" + id + " Recipe deleted succesfully");
    }

    @Override
    public ApiResponse addRecipe(String name, MultipartFile file) {

        Recipe recipe = new Recipe(name);
        recipeRepository.save(recipe);

        ApiResponse apiResponse = uploadFile(recipe, file);
        if (!apiResponse.isSuccess())
            return new ApiResponse("Some error to upload file", false, apiResponse.getMessage() + "/" + apiResponse.getObject());
        return new ApiResponse("Recipe succesfully added", true, "Recipe succesfully added");


    }

    @Override
    public ApiResponse getById(Long id) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        if (recipeOptional.isEmpty())
            return new ApiResponse("Not found Recipe", false, "Not found Recipe");
        return new ApiResponse("Recipe id:" + id, true, recipeRepository.getById(id));
    }

    @Override
    public ApiResponse addIngredientToRecipe(Long recipeId, List<Long> ingredientIdList) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);
        if (optionalRecipe.isEmpty()) return new ApiResponse("recipe not found", false, "Recipe not found");

        List<Ingredient> ingredientList = ingredientRepository.findAllById(ingredientIdList);
        List<RecipeIngredient> recipeIngredientList = new ArrayList<>();

        for (Ingredient ingredient : ingredientList) {
            recipeIngredientList.add(
                    new RecipeIngredient(
                            optionalRecipe.get(),
                            ingredient
                    )
            );
        }

        recipeIngredientRepository.saveAll(recipeIngredientList);

        return new ApiResponse("Saccesfully attached", true, "Succesfully attached");
    }

    @SneakyThrows
    private ApiResponse uploadFile(Recipe recipe, MultipartFile photoFile) {
        String contentType = photoFile.getContentType();
        String[] split = contentType.split("/");

        if (!(split[1].equals("jpeg") || split[1].equals("png")))
            return new ApiResponse("File type is uncorrectly. Upload jpg or png file", false,
                    "File type is uncorrectly. Upload jpg or png file");

        File file = new File(uploadPath);
        if (!file.exists()) {
            file.mkdir();
        }

        String uuid = UUID.randomUUID().toString();
        String fileUUIDName = uuid + "." + photoFile.getOriginalFilename();

        Path path = Path.of(uploadPath + "/" + fileUUIDName);

        Files.copy(photoFile.getInputStream(), path);

        Photo photo = new Photo(fileUUIDName);
        Photo savedPhoto = photoRepository.save(photo);
        recipe.setPhoto(savedPhoto);
        recipeRepository.save(recipe);


        return new ApiResponse("Upload file succesfully", true, "upload file succesfully");

    }
}
