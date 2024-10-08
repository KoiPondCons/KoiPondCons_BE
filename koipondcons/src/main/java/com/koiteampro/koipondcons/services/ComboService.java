package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.entities.Combo;
import com.koiteampro.koipondcons.exception.NotFoundException;
import com.koiteampro.koipondcons.models.request.ComboRequest;
import com.koiteampro.koipondcons.repositories.ComboRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComboService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private ComboRepository comboRepository;

    public Combo createCombo(ComboRequest comboRequest) {
        Combo combo = modelMapper.map(comboRequest, Combo.class);
        comboRepository.save(combo);
        return combo;
    }

    public List<Combo> getAllCombos() {
        return comboRepository.findAllByIsDeletedFalse();
    }

    public Combo getComboById(Long id) {
        Optional<Combo> combo = comboRepository.findById(id);

        if (combo.isPresent()) {
            return combo.get();
        } else {
            throw new NotFoundException("Combo not found");
        }
    }

    public Combo updateCombo(Long id, ComboRequest comboRequest) {
        Combo combo = modelMapper.map(comboRequest, Combo.class);
        combo.setId(id);
        comboRepository.save(combo);
        return combo;
    }

    public void deleteCombo(Long id) {
        Combo combo = getComboById(id);
        combo.setDeleted(true);
        comboRepository.save(combo);
    }

}
