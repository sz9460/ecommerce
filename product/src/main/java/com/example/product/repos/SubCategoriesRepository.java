package com.example.product.repos;

import com.example.product.entity.SubCategories;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SubCategoriesRepository extends MongoRepository<SubCategories,String> {
  Optional<SubCategories> findBySubcategoryName(String name);
}
