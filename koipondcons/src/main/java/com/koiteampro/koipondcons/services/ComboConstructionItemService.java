package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.entities.ComboConstructionItem;
import com.koiteampro.koipondcons.exception.NotFoundException;
import com.koiteampro.koipondcons.models.request.ComboConstructionItemRequest;
import com.koiteampro.koipondcons.models.response.ComboConstructionItemResponse;
import com.koiteampro.koipondcons.repositories.ComboConstructionItemRepository;
import com.koiteampro.koipondcons.repositories.ComboPriceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ComboConstructionItemService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private ComboConstructionItemRepository comboConstructionItemRepository;
    @Autowired
    private ComboPriceRepository comboPriceRepository;

    public ComboConstructionItemResponse addComboConstructionItem(ComboConstructionItemRequest comboConstructionItemRequest) {
        ComboConstructionItem comboConstructionItem = modelMapper.map(comboConstructionItemRequest, ComboConstructionItem.class);
        comboConstructionItemRepository.save(comboConstructionItem);
        return modelMapper.map(comboConstructionItem, ComboConstructionItemResponse.class);
    }

    public List<ComboConstructionItemResponse> getAllComboConstructionItemsByComboId(long comboId) {
        List<ComboConstructionItem> comboConstructionItems = comboConstructionItemRepository.findByComboIdAndIsDisabledFalse(comboId);
        return comboConstructionItems.stream().map(c -> modelMapper.map(c, ComboConstructionItemResponse.class)).collect(Collectors.toList());
    }

    public ComboConstructionItemResponse getComboConstructionItemById(long id) {
        ComboConstructionItem comboConstructionItem = comboConstructionItemRepository.findById(id);

        if (comboConstructionItem != null) {
            return modelMapper.map(comboConstructionItem, ComboConstructionItemResponse.class);
        } else {
            throw new NotFoundException("Combo construction item not found");
        }
    }

    public ComboConstructionItemResponse updateComboConstructionItem(long id, ComboConstructionItemRequest comboConstructionItemRequest) {
        ComboConstructionItem comboConstructionItem = modelMapper.map(comboConstructionItemRequest, ComboConstructionItem.class);
        comboConstructionItem.setId(id);
        comboConstructionItemRepository.save(comboConstructionItem);
        return modelMapper.map(comboConstructionItem, ComboConstructionItemResponse.class);
    }

    public boolean deleteComboConstructionItem(long id) {
        ComboConstructionItem comboConstructionItem = comboConstructionItemRepository.findById(id);
        if(comboConstructionItem != null){
            comboConstructionItem.setDisabled(true);
            comboConstructionItemRepository.save(comboConstructionItem);
            return true;
        }
        return false;
    }


}
