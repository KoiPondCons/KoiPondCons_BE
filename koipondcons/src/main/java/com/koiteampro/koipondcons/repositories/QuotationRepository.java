package com.koiteampro.koipondcons.repositories;

import com.koiteampro.koipondcons.entities.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuotationRepository extends JpaRepository<Quotation, Long> {
}
