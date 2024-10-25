package com.koiteampro.koipondcons.repositories;

import com.koiteampro.koipondcons.entities.ComboConstructionItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComboConstructionItemRepository extends JpaRepository<ComboConstructionItem, Long> {
    public List<ComboConstructionItem> findByComboId(Long comboId);
}
