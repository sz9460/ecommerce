package com.example.product.controller;

import com.example.product.entity.Category;
import com.example.product.entity.Product;
import com.example.product.entity.SubCategories;
import com.example.product.model.ProductRequest;
import com.example.product.repos.CategoryRepository;
import com.example.product.repos.ProductImageRepository;
import com.example.product.repos.ProductRepository;
import com.example.product.repos.SubCategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class ProductController {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    SubCategoriesRepository subCategoriesRepository;
    @Autowired
    ProductImageRepository productImageRepository;

    @PostMapping("/addProduct")
    @PreAuthorize("hasRole('ADMIN')")
    public Object addProduct(@RequestBody ProductRequest productRequest){
        if (productRepository.findByNameProduct(productRequest.getNameProduct()).isEmpty()){
            try{
                Product product = new Product();
                SubCategories subCategories =  subCategoriesRepository.findBySubcategoryName(productRequest.getSubCategories()).get();
                Category category =categoryRepository.findByCategoryName(productRequest.getCategory()).get();
                product.setNameProduct(productRequest.getNameProduct());
                product.setDescription(productRequest.getDescription());
                product.setPrix(productRequest.getPrix());
                product.setCategory(category.getCategoryName());
                product.setQuantityStock(productRequest.getQuantityStock());
                product.setSubCategories(subCategories.getSubcategoryName());
                product.setProductImage(productImageRepository.findByName(productRequest.getImageName()));

                product =productRepository.save(product);
                return ResponseEntity.ok(product);
            }
            catch (Exception  e){
                return  new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
            }
        }
        else {
            return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
        }

    }
    @GetMapping("/allProducts")
    public  List<Product> getAllProduct(){
        return productRepository.findAll();
    }
    @GetMapping("/{id}")
    public Object getProductById(@PathVariable String id){
        if (productRepository.findById(id).isPresent()){
            Product product =productRepository.findById(id).get();
            return ResponseEntity.ok(product);
        }
        else {
            return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void DeleteProduct(@PathVariable String id){productRepository.deleteById(id);
    }
    @GetMapping("/{category}/{subCategory}")
    public  List<Product> getProductByCategoryAndSubCategory(@PathVariable String category ,@PathVariable String subCategory){
        return productRepository.findProductsByCategoryAndSubCategories(category,subCategory);
    }



}
