package com.example.payement.repos;

import com.example.payement.entity.Payement;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PayementRepos extends JpaRepository<Payement, Integer> {
    Payement findPayementByPayementId(String payementId);
}
