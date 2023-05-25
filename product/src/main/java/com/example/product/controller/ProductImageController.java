package com.example.product.controller;

import com.example.product.Service.ProductImageService;
import com.example.product.entity.ProductImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class ProductImageController {
    @Autowired
    private ProductImageService productImageService;

    @ResponseStatus (value = HttpStatus.OK)
    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ProductImage uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        return productImageService.uploadImage(file);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> downloadImage(@PathVariable String fileName) throws IOException {
        byte[] image = productImageService.downloadImage(fileName);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(image);
    }
}
