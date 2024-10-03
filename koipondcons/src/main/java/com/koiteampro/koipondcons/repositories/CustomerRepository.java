package com.koiteampro.koipondcons.repositories;

import com.koiteampro.koipondcons.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByAccountId(long accountId);
}
