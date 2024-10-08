package com.koiteampro.koipondcons.repositories;

import com.koiteampro.koipondcons.entities.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    
}
