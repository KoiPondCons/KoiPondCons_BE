package com.koiteampro.koipondcons.repositories;

import com.koiteampro.koipondcons.entities.Combo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ComboRepository extends JpaRepository<Combo, Long> {

    List<Combo> findAllByIsDeletedFalse();

    @Query(
            "select cb.name, count(*) as totalOrders\n" +
                    "from Quotation q join q.constructionOrder co join q.combo cb\n" +
                    "where co.status = 'CLOSED'\n" +
                    "group by q.combo.id"
    )
    List<Object[]> countOrdersOfCombo();
}
