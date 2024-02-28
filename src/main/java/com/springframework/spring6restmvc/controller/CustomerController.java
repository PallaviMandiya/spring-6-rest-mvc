package com.springframework.spring6restmvc.controller;

import com.springframework.spring6restmvc.model.Customer;
import com.springframework.spring6restmvc.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
//@RequestMapping("/api/v1/customer")
@RestController
public class CustomerController {
    private final CustomerService customerService;

    public static final String CUSTOMER_PATH = "/api/v1/customer";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";

    @GetMapping(CUSTOMER_PATH_ID)
    public Customer getCustomerById(@PathVariable("customerId") UUID customerId){
        log.debug("Get Customer By Id : Customer Controller");
        return customerService.getCustomerById(customerId).orElseThrow(NotFoundException::new);
    }

    @GetMapping(CUSTOMER_PATH)
    public List<Customer> getAllCustomers(){
        log.debug("Get Al Customers : Customer Controller");
        return customerService.listCustomer();
    }
    @PostMapping(CUSTOMER_PATH)
    public ResponseEntity createCustomer(@RequestBody Customer customer){
        Customer savedCustomer = customerService.saveNewCustomer(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location",CUSTOMER_PATH + "/" + savedCustomer.getId().toString());
        return new ResponseEntity(headers,HttpStatus.CREATED);
    }
    @PutMapping(CUSTOMER_PATH_ID)
    public ResponseEntity updateById(@PathVariable("customerId")UUID customerId,@RequestBody Customer customer){
        customerService.updateCustomerById(customerId, customer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location",CUSTOMER_PATH + "/" + customerId);
        return new ResponseEntity(headers,HttpStatus.NO_CONTENT);
    }
    @DeleteMapping(CUSTOMER_PATH_ID)
    public ResponseEntity deleteById(@PathVariable("customerId") UUID customerId){
        customerService.deleteCustomerById(customerId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(CUSTOMER_PATH_ID)
    public ResponseEntity patchById(@PathVariable("customerId")UUID customerId,@RequestBody Customer customer){
        customerService.patchCustomerById(customerId,customer);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
