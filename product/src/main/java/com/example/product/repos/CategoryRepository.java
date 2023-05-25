package com.example.product.repos;

import com.example.product.entity.Category;
import com.example.product.entity.SubCategories;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CategoryRepository extends MongoRepository<Category,String> {
     Optional<Category> findByCategoryName(String name);

}
