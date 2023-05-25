package com.example.product.repos;

import com.example.product.entity.ProductImage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductImageRepository  extends MongoRepository<ProductImage,String> {
    ProductImage findByName(String fileName);

}
