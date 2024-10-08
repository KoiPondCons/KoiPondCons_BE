package com.koiteampro.koipondcons.repositories;

import com.koiteampro.koipondcons.entities.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    List<Promotion> findAllByPointsAvailableLessThanEqual(int pointsAvailable);
}
