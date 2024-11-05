package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.entities.ComboPrice;
import com.koiteampro.koipondcons.exception.NotFoundException;
import com.koiteampro.koipondcons.models.request.ComboPriceRequest;
import com.koiteampro.koipondcons.models.response.ComboPriceResponse;
import com.koiteampro.koipondcons.repositories.ComboPriceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ComboPriceService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ComboPriceRepository comboPriceRepository;

    public ComboPriceResponse addComboPrice(ComboPriceRequest comboPriceRequest) {
        ComboPrice comboPrice = modelMapper.map(comboPriceRequest, ComboPrice.class);
        comboPriceRepository.save(comboPrice);
        return modelMapper.map(comboPrice, ComboPriceResponse.class);
    }

    public List<ComboPriceResponse> getComboPricesByComboId(long comboId) {
        List<ComboPrice> comboPrices = comboPriceRepository.findByComboId(comboId);
        return comboPrices.stream().map(c -> modelMapper.map(c, ComboPriceResponse.class)).collect(Collectors.toList());
    }

    public ComboPriceResponse getComboPriceById(long comboId) {
        ComboPrice comboPrice = comboPriceRepository.findById(comboId);

        if (comboPrice != null) {
            return modelMapper.map(comboPrice, ComboPriceResponse.class);
        } else {
            throw new NotFoundException("Couldn't find combo price");
        }
    }

    public ComboPriceResponse updateComboPrice(long comboId, ComboPriceRequest comboPriceRequest) {
        ComboPrice comboPrice = modelMapper.map(comboPriceRequest, ComboPrice.class);
        comboPrice.setId(comboId);
        comboPriceRepository.save(comboPrice);
        return modelMapper.map(comboPrice, ComboPriceResponse.class);
    }

    public boolean deleteComboPrice(long comboId) {
        ComboPrice comboPrice = comboPriceRepository.findById(comboId);
        if (comboPrice != null) {
            comboPrice.setDisabled(true);
            comboPriceRepository.save(comboPrice);
            return true;
        }
        return false;
    }

    public ComboPriceResponse getCompoPriceByComboIdAndVolume(long comboId, float volume) {
        ComboPrice comboPrice = comboPriceRepository.findByComboIdAndMinVolumeLessThanEqualAndMaxVolumeGreaterThanEqual(comboId, volume, volume);
        if (comboPrice != null) {
            return modelMapper.map(comboPrice, ComboPriceResponse.class);
        } else {
            throw new NotFoundException("Combo or Combo Price not found");
        }
    }
}
