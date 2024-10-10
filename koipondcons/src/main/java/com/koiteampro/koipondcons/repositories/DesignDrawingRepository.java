package com.koiteampro.koipondcons.repositories;

import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.entities.DesignDrawing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DesignDrawingRepository extends JpaRepository<DesignDrawing, Long> {
    List<DesignDrawing> findAllByDesignerAccountId(Long id);
    List<Long> findDesignerAccountIdByStatusNotLike(String status);
}
