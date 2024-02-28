package com.springframework.spring6restmvc.services;

import com.springframework.spring6restmvc.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    Optional<Customer> getCustomerById(UUID id);
    List<Customer> listCustomer();

    Customer saveNewCustomer(Customer customer);

    void updateCustomerById(UUID customerId, Customer customer);

    void deleteCustomerById(UUID customerId);

    void patchCustomerById(UUID customerId, Customer customer);
}
