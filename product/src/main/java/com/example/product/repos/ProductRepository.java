package com.example.product.repos;

import com.example.product.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product,String> {
    Optional<Product> findByNameProduct(String s);

    List<Product> findProductsByCategoryAndSubCategories(String category, String subCategory);
}
