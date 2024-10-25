package com.koiteampro.koipondcons.repositories;

import com.koiteampro.koipondcons.entities.Combo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComboRepository extends JpaRepository<Combo, Long> {

    List<Combo> findAllByIsDeletedFalse();
}
