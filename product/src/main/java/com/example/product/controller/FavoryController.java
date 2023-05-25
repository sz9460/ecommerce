package com.example.product.controller;

import com.example.product.Service.FavoryService;
import com.example.product.entity.Favory;
import com.example.product.repos.FavoryRepository;
import com.example.product.repos.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favory")
public class FavoryController {

    @Autowired
    FavoryService favoryService;

    @GetMapping("/myFavory")
    @PreAuthorize("hasRole('USER')")
    public  Favory getMyFavory(@NonNull HttpServletRequest request){
        return favoryService.getMyFavory(request);
    }
    @PostMapping("/addtoFavory/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<HttpStatus> addTofavory(@PathVariable("id") String id,@NonNull HttpServletRequest request){
    return favoryService.addTofavory(id, request) ;
    }
    @DeleteMapping("/deleteFromFavory/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<HttpStatus> deleteFromFavory(@PathVariable("id") String id,@NonNull HttpServletRequest request){
        return favoryService.deleteFromfavory(id, request) ;
    }


}
