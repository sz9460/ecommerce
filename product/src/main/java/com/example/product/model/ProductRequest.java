package com.example.product.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private String nameProduct;
    private String description;
    private  Integer quantityStock;
    private  Float prix;
    private String category;
    private String subCategories;
    private String imageName;
}
