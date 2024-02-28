package com.springframework.spring6restmvc.services;

import com.springframework.spring6restmvc.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {
    private Map<UUID,Customer> customerMap;

    public CustomerServiceImpl() {
        this.customerMap = new HashMap<>();
        Customer customer1 = Customer.builder()
                .id(UUID.randomUUID())
                .CustomerName("Pal")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer customer2 = Customer.builder()
                .id(UUID.randomUUID())
                .CustomerName("Prezi")
                .version(2)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer customer3 = Customer.builder()
                .id(UUID.randomUUID())
                .CustomerName("Sneha")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        customerMap.put(customer1.getId(), customer1);
        customerMap.put(customer2.getId(),customer2);
        customerMap.put(customer3.getId(),customer3);
    }
    @Override
    public List<Customer> listCustomer() {
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Customer saveNewCustomer(Customer customer) {
        Customer savedCustomer = Customer.builder()
                .id(UUID.randomUUID())
                .CustomerName(customer.getCustomerName())
                .version(customer.getVersion())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        customerMap.put(savedCustomer.getId(), savedCustomer);
        return savedCustomer;
    }

    @Override
    public void updateCustomerById(UUID customerId, Customer customer) {
        Customer existing = customerMap.get(customerId);
        existing.setCustomerName(customer.getCustomerName());
        existing.setVersion(customer.getVersion());
//        customerMap.put(existing.getId(),existing);
    }

    @Override
    public void deleteCustomerById(UUID customerId) {
        customerMap.remove(customerId);
    }

    @Override
    public void patchCustomerById(UUID customerId, Customer customer) {
        Customer existing = customerMap.get(customerId);
        if(StringUtils.hasText(customer.getCustomerName())){
            existing.setCustomerName(customer.getCustomerName());
        }
        if(customer.getVersion() != null){
            existing.setVersion(customer.getVersion());
        }
    }

    @Override
    public Optional<Customer> getCustomerById(UUID id) {
        log.debug("GetCustomerByID- Customer Service Impl "+id);
        System.out.println(customerMap.get(id));
        return Optional.of(customerMap.get(id));
    }

//    @Override
//    public Customer getCustomerById(UUID id) {
//        log.debug("GetCustomerByID- Customer Service Impl "+id);
//        return Customer.builder()
//                .id(UUID.randomUUID())
//                .CustomerName("Sneha")
//                .version(1)
//                .createdDate(LocalDateTime.now())
//                .lastModifiedDate(LocalDateTime.now())
//                .build();
//    }


}
