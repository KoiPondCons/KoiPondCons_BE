package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.entities.Promotion;
import com.koiteampro.koipondcons.exception.NotFoundException;
import com.koiteampro.koipondcons.models.request.PromotionRequest;
import com.koiteampro.koipondcons.repositories.PromotionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PromotionService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PromotionRepository promotionRepository;

    public Promotion createPromotion(PromotionRequest promotionRequest) {
        Promotion promotion = modelMapper.map(promotionRequest, Promotion.class);
        promotionRepository.save(promotion);
        return promotion;
    }

    public List<Promotion> getPromotions() {
        return promotionRepository.findAll();
    }

    public Promotion getPromotionById(Long id) {
        Optional<Promotion> promotion = promotionRepository.findById(id);

        if (promotion.isPresent()) {
            return promotion.get();
        } else {
            throw new NotFoundException("Promotion not found");
        }
    }

    public Promotion updatePromotion(Long id, PromotionRequest promotionRequest) {
        Promotion promotion = modelMapper.map(promotionRequest, Promotion.class);
        promotion.setId(id);
        promotionRepository.save(promotion);
        return promotion;
    }
}
