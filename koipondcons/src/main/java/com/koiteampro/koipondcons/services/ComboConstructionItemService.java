package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.entities.ComboConstructionItem;
import com.koiteampro.koipondcons.exception.NotFoundException;
import com.koiteampro.koipondcons.models.request.ComboConstructionItemRequest;
import com.koiteampro.koipondcons.repositories.ComboConstructionItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComboConstructionItemService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private ComboConstructionItemRepository comboConstructionItemRepository;

    public ComboConstructionItem addComboConstructionItem(ComboConstructionItemRequest comboConstructionItemRequest) {
        ComboConstructionItem comboConstructionItem = modelMapper.map(comboConstructionItemRequest, ComboConstructionItem.class);
        comboConstructionItemRepository.save(comboConstructionItem);
        return comboConstructionItem;
    }

    public List<ComboConstructionItem> getAllComboConstructionItemsByComboId(long comboId) {
        return comboConstructionItemRepository.findByComboId(comboId);
    }

    public ComboConstructionItem getComboConstructionItemById(long id) {
        Optional<ComboConstructionItem> optionalComboConstructionItem = comboConstructionItemRepository.findById(id);

        if (optionalComboConstructionItem.isPresent()) {
            return optionalComboConstructionItem.get();
        } else {
            throw new NotFoundException("Combo construction item not found");
        }
    }

    public ComboConstructionItem updateComboConstructionItem(long id, ComboConstructionItemRequest comboConstructionItemRequest) {
        ComboConstructionItem comboConstructionItem = modelMapper.map(comboConstructionItemRequest, ComboConstructionItem.class);
        comboConstructionItem.setId(id);
        comboConstructionItemRepository.save(comboConstructionItem);
        return comboConstructionItem;
    }

    public void deleteComboConstructionItem(long id) {
        comboConstructionItemRepository.deleteById(id);
    }




}
