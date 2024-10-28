package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.entities.Customer;
import com.koiteampro.koipondcons.entities.PointHistory;
import com.koiteampro.koipondcons.enums.PointAction;
import com.koiteampro.koipondcons.exception.NotFoundException;
import com.koiteampro.koipondcons.repositories.CustomerRepository;
import com.koiteampro.koipondcons.repositories.PointHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PointHistoryRepository pointHistoryRepository;
    @Autowired
    AccountService accountService;

    public Customer getCurrentCustomer() {
        Account account = accountService.getCurrentAccount();
        return customerRepository.findByAccountId(account.getId());
    }

    public Customer getCustomerById(long id) {
        Optional<Customer> customer = customerRepository.findById(id);

        if (customer.isPresent()) {
            return customer.get();
        } else {
            throw new NotFoundException("Customer with id " + id + " not found");
        }
    }

    public void addTotalPoint(long id, int amount) {
        Optional<Customer> customer = customerRepository.findById(id);

        if (customer.isPresent()) {
            Customer customerToAdd = customer.get();
            customerToAdd.setTotal_points(customerToAdd.getTotal_points() + amount);
            PointHistory pointHistory = new PointHistory();
            pointHistory.setPoints(customerToAdd.getTotal_points());
            pointHistory.setCustomer(customerToAdd);
            pointHistory.setPointAction(PointAction.ADD);
            pointHistory.setContent("Add Point");
            pointHistory.setCreateAt(LocalDateTime.now());
            pointHistoryRepository.save(pointHistory);
            customerRepository.save(customerToAdd);
        } else {
            throw new NotFoundException("Customer with id " + id + " not found");
        }
    }

    public void minusTotalPoint(long id, int amount) {
        Optional<Customer> customer = customerRepository.findById(id);

        if (customer.isPresent()) {
            Customer customerToAdd = customer.get();
            customerToAdd.setTotal_points(customerToAdd.getTotal_points() - amount);
            PointHistory pointHistory = new PointHistory();
            pointHistory.setPoints(customerToAdd.getTotal_points());
            pointHistory.setCustomer(customerToAdd);
            pointHistory.setPointAction(PointAction.SUBTRACT);
            pointHistory.setContent("Subtract Point");
            pointHistory.setCreateAt(LocalDateTime.now());
            pointHistoryRepository.save(pointHistory);
            customerRepository.save(customerToAdd);
        } else {
            throw new NotFoundException("Customer with id " + id + " not found");
        }
    }
}
