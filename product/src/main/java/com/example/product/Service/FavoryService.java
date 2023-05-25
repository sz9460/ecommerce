package com.example.product.Service;

import com.example.product.entity.Favory;
import com.example.product.entity.Product;
import com.example.product.repos.FavoryRepository;
import com.example.product.repos.ProductRepository;
import com.example.product.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class FavoryService {
    @Autowired
    FavoryRepository favoryRepository;
    @Autowired
    ProductRepository productRepository;

    @Autowired
    JwtService jwtService;

    public ResponseEntity<HttpStatus> addTofavory(String id , @NonNull HttpServletRequest request){
        Favory favory = favoryRepository.findByIdUser(jwtService.exctractUserIdFromRequest(request));
        Product product =productRepository.findById(id).get();
        favory.getProducts().add(product);
        favoryRepository.save(favory);

        return new ResponseEntity<HttpStatus>(HttpStatus.OK);

    }

    public ResponseEntity<HttpStatus> deleteFromfavory(String id , @NonNull HttpServletRequest request){
        Favory favory = favoryRepository.findByIdUser(jwtService.exctractUserIdFromRequest(request));
        Set<Product> products = favory.getProducts();
        Product product1 = null;
        for(Product product : products){
            if(product.getId().equals(id)){
                product1 = product;
                break;
            }
        }
        products.remove(product1);
        favory.setProducts(products);
        favoryRepository.save(favory);
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);

    }
    public  Favory getMyFavory(@NonNull HttpServletRequest request){
        return favoryRepository.findByIdUser(jwtService.exctractUserIdFromRequest(request));
    }

}
