package com.koiteampro.koipondcons.repositories;

import com.koiteampro.koipondcons.entities.ComboPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComboPriceRepository extends JpaRepository<ComboPrice, Long> {
    public List<ComboPrice> findByComboId(long comboId);

    public ComboPrice findByComboIdAndMinVolumeLessThanEqualAndMaxVolumeGreaterThanEqual(long comboId, float minVolume, float maxVolume);
}
