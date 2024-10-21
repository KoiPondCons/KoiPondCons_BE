package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.entities.ComboPrice;
import com.koiteampro.koipondcons.exception.NotFoundException;
import com.koiteampro.koipondcons.models.request.ComboPriceRequest;
import com.koiteampro.koipondcons.repositories.ComboPriceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComboPriceService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ComboPriceRepository comboPriceRepository;

    public ComboPrice addComboPrice(ComboPriceRequest comboPriceRequest) {
        ComboPrice comboPrice = modelMapper.map(comboPriceRequest, ComboPrice.class);
        comboPriceRepository.save(comboPrice);
        return comboPrice;
    }

    public List<ComboPrice> getComboPricesByComboId(long comboId) {
        return comboPriceRepository.findByComboId(comboId);
    }

    public ComboPrice getComboPriceById(long comboId) {
        Optional<ComboPrice> optionalComboPrice = comboPriceRepository.findById(comboId);

        if (optionalComboPrice.isPresent()) {
            return optionalComboPrice.get();
        } else {
            throw new NotFoundException("Couldn't find combo price");
        }
    }

    public ComboPrice updateComboPrice(long comboId, ComboPriceRequest comboPriceRequest) {
        ComboPrice comboPrice = modelMapper.map(comboPriceRequest, ComboPrice.class);
        comboPrice.setId(comboId);
        comboPriceRepository.save(comboPrice);
        return comboPrice;
    }

    public void deleteComboPrice(long comboId) {
        comboPriceRepository.deleteById(comboId);
    }

    public ComboPrice getCompoPriceByComboIdAndVolume(long comboId, float volume) {
        ComboPrice comboPrice = comboPriceRepository.findByComboIdAndMinVolumeLessThanEqualAndMaxVolumeGreaterThanEqual(comboId, volume, volume);
        if (comboPrice != null) {
            return comboPrice;
        } else {
            throw new NotFoundException("Combo or Combo Price not found");
        }
    }
}
