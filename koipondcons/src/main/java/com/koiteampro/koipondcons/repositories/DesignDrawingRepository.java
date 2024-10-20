package com.koiteampro.koipondcons.repositories;

import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.entities.DesignDrawing;
import com.koiteampro.koipondcons.enums.DesignDrawingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DesignDrawingRepository extends JpaRepository<DesignDrawing, Long> {
    List<DesignDrawing> findAllByDesignerAccountId(Long id);

    @Query(
            "select d.designerAccount.id\n" +
                    "from DesignDrawing d\n" +
                    "where d.status != :status" +
                    "\n and d.designerAccount.id is not null"
    )
    List<Long> findStaffIdsWithUnfinishedWorks(DesignDrawingStatus status);
}
