package com.example.product.controller;
import com.example.product.entity.SubCategories;
import com.example.product.repos.SubCategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/SubCategory")
@PreAuthorize("hasRole('ADMIN')")
public class SubCatoryController {
    @Autowired
    SubCategoriesRepository subCategoriesRepository;

    @PostMapping("/addSubCategory")
    public ResponseEntity< SubCategories > addCategory(@RequestBody SubCategories subCategories){
        if(subCategoriesRepository.findBySubcategoryName(subCategories.getSubcategoryName()).isEmpty()){
            SubCategories subCategories1 =new SubCategories();
            subCategories1.setSubcategoryName(subCategories.getSubcategoryName());
            return ResponseEntity.ok(subCategoriesRepository.save(subCategories1));
        }
        else {
            return  null;
        }


    }
    @GetMapping("/getAllSubCategories")
    public List<SubCategories> getAllSubCategoriers(){
        return subCategoriesRepository.findAll();
    }

    @GetMapping("/getSubCategorie/{id}")
    public SubCategories getSubCategorie(@PathVariable String id){
        return subCategoriesRepository.findById(id).get();
    }


    @PutMapping("/update/{id}")
    public  Object updateSubCategory(@PathVariable String id ,@RequestBody SubCategories subCategories){
        if(subCategoriesRepository.findById(id).isPresent()){
            subCategories.setId(id);
            return ResponseEntity.ok( subCategoriesRepository.save(subCategories));

        }
        return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/delelte/{id}")
    public void DeleteCategory(@PathVariable String id){subCategoriesRepository.deleteById(id);}


}