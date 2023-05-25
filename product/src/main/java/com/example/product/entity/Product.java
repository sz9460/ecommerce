package com.example.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Product {
    @Id
    private String id;
    private String nameProduct;
    private String description;
    private  Float prix;

    private  Integer QuantityStock;
    private String category;
    private String subCategories;
    @DBRef
    private ProductImage productImage;

}
