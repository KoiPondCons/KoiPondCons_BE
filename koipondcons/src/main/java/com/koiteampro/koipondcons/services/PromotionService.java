package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.entities.Customer;
import com.koiteampro.koipondcons.entities.Promotion;
import com.koiteampro.koipondcons.exception.NotFoundException;
import com.koiteampro.koipondcons.models.request.PromotionRequest;
import com.koiteampro.koipondcons.repositories.CustomerRepository;
import com.koiteampro.koipondcons.repositories.PromotionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PromotionService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PromotionRepository promotionRepository;

    @Autowired
    CustomerRepository customerRepository;

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

    public List<Promotion> getPromotionsByCustomerAvailableForCustomer(long customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        List<Promotion> promotions = new ArrayList<>();

        if (customer.isPresent()) {
            Customer customer1 = customer.get();
            int point = customer1.getTotal_points();
            promotions = promotionRepository.findAllByPointsAvailableLessThanEqualOrderByPointsAvailable(point);
            List<Promotion> promotionList = new ArrayList<>();
            if (promotions.size() > 1) {
                promotionList.add(promotions.getFirst());
                promotionList.add(promotions.getLast());
            }
            else {
                promotionList.add(promotions.getFirst());
            }
            return promotionList;
        } else {
            throw new NotFoundException("Customer not found");
        }
    }
}
