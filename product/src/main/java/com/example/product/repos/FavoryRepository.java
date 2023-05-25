package com.example.product.repos;

import com.example.product.entity.Category;
import com.example.product.entity.Favory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FavoryRepository extends MongoRepository<Favory,String> {
    Favory findByIdUser (Integer id);
}
