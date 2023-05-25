package com.example.product.controller;

import com.example.product.entity.Category;
import com.example.product.entity.SubCategories;
import com.example.product.model.CategoryRequest;
import com.example.product.repos.CategoryRepository;
import com.example.product.repos.SubCategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    SubCategoriesRepository subCategoriesRepository;

    @PostMapping("/addCategory")
    @PreAuthorize("hasRole('ADMIN')")
    public Object addCategory(@RequestBody CategoryRequest categoryRequest){
        if(categoryRepository.findByCategoryName(categoryRequest.getCategoryName()).isEmpty()){
            Category category1 =new Category();
            return ResponseEntity.ok(getCategory(categoryRequest, category1));
        }
        else {
            return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
        }

    }
    @GetMapping("/getAllCategories")
    public List<Category> getAllCategoriers(){
        return categoryRepository.findAll();
    }
    @GetMapping("/getCategorie/{id}")
    public Category getAllCategorie(@PathVariable String id){
        return categoryRepository.findById(id).get();
    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public  Object updateCategory(@PathVariable String id ,@RequestBody CategoryRequest categoryRequest){
        if(categoryRepository.findById(id).isPresent()){
            Category category = categoryRepository.findById(id).get();
            category.setId(id);
            return ResponseEntity.ok(getCategory(categoryRequest, category));
        }
        return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
    }

    private Category getCategory(@RequestBody CategoryRequest categoryRequest, Category category) {
        List<SubCategories> subCategoriesList = new ArrayList<>();
        category.setCategoryName(categoryRequest.getCategoryName());
        categoryRequest.getSubCategories().forEach((item)-> subCategoriesList.add(subCategoriesRepository.findBySubcategoryName(item).get()));
        category.setSubCategories(subCategoriesList);
        return categoryRepository.save(category);
    }

    @DeleteMapping("/delelte/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void DeleteCategory(@PathVariable String id){
        categoryRepository.deleteById(id);
    }

    @GetMapping("/{categorie}/getSubCategorie")
    Object getSubCategorie(@PathVariable String categorie){

        if( categoryRepository.findByCategoryName(categorie).isPresent()){
            Category category= categoryRepository.findByCategoryName(categorie).get();
            List<String> sub =new ArrayList<>();
            category.getSubCategories().forEach((item)->sub.add(item.getSubcategoryName()));
            return  ResponseEntity.ok(sub);
        }
        else {
            return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
        }

    }


}
