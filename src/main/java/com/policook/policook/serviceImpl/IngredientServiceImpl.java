package com.policook.policook.serviceImpl;

import com.policook.policook.entity.Ingredient;
import com.policook.policook.entity.Photo;
import com.policook.policook.payload.ApiResponse;
import com.policook.policook.repository.IngredientRepository;
import com.policook.policook.repository.PhotoRepository;
import com.policook.policook.service.IngredientService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

@Service
public class IngredientServiceImpl implements IngredientService {
    @Value("${upload.ingredients.path}")
    String uploadPath;

    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    PhotoRepository photoRepository;

    @Override
    public ApiResponse get() {
        if (ingredientRepository.findAll().size() != 0)
            return new ApiResponse("Ingredient List:", true, ingredientRepository.findAll());
        else return new ApiResponse("List is empty", false, "List is Empty");
    }

    @Override
    public ApiResponse delete(Long id) {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findById(id);
        if (optionalIngredient.isEmpty()) return new ApiResponse("Ingredient not found", false, "Ingredient not found");

        String photo = optionalIngredient.get().getPhoto().getName();
        File file = new File(uploadPath + "/" + photo);
        if (!file.delete()) {
            return new ApiResponse("Photo don;t delete", false, "Photo don't delete");
        }
        ingredientRepository.deleteById(id);
        return new ApiResponse("Ingredient deleted", true, "id=" + id + " ingredient deleted succesfully");
    }

    @Override
    public ApiResponse addIngredient(String name, MultipartFile file) {

        Ingredient ingredient = new Ingredient(name);
        ingredientRepository.save(ingredient);

        ApiResponse apiResponse = uploadFile(ingredient, file);
        if (!apiResponse.isSuccess())
            return new ApiResponse("Some error to upload file", false, apiResponse.getMessage() + "/" + apiResponse.getObject());
        return new ApiResponse("Ingredient succesfully added", true, "Ingredient succesfully added");


    }

    @Override
    public ApiResponse getById(Long id) {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findById(id);
        if (optionalIngredient.isEmpty())
            return new ApiResponse("Not found Ingredient", false, "Not found Ingredient");
        return new ApiResponse("Ingredient id:" + id, true, ingredientRepository.getById(id));
    }

    @SneakyThrows
    private ApiResponse uploadFile(Ingredient ingredient, MultipartFile photoFile) {
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
        ingredient.setPhoto(savedPhoto);
        ingredientRepository.save(ingredient);


        return new ApiResponse("Upload file succesfully", true, "upload file succesfully");

    }
}
