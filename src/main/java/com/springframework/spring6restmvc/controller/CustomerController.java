package com.springframework.spring6restmvc.controller;

import com.springframework.spring6restmvc.model.CustomerDTO;
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
    public CustomerDTO getCustomerById(@PathVariable("customerId") UUID customerId){
        log.debug("Get Customer By Id : Customer Controller");
        return customerService.getCustomerById(customerId).orElseThrow(NotFoundException::new);
    }

    @GetMapping(CUSTOMER_PATH)
    public List<CustomerDTO> getAllCustomers(){
        log.debug("Get Al Customers : Customer Controller");
        return customerService.listCustomer();
    }
    @PostMapping(CUSTOMER_PATH)
    public ResponseEntity createCustomer(@RequestBody CustomerDTO customer){
        CustomerDTO savedCustomer = customerService.saveNewCustomer(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location",CUSTOMER_PATH + "/" + savedCustomer.getId().toString());
        return new ResponseEntity(headers,HttpStatus.CREATED);
    }
    @PutMapping(CUSTOMER_PATH_ID)
    public ResponseEntity updateById(@PathVariable("customerId")UUID customerId,@RequestBody CustomerDTO customer){
        if(customerService.updateCustomerById(customerId, customer).isEmpty()){
            throw new NotFoundException();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location",CUSTOMER_PATH + "/" + customerId);
        return new ResponseEntity(headers,HttpStatus.NO_CONTENT);
    }
    @DeleteMapping(CUSTOMER_PATH_ID)
    public ResponseEntity deleteById(@PathVariable("customerId") UUID customerId){
        if(! customerService.deleteCustomerById(customerId)){
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(CUSTOMER_PATH_ID)
    public ResponseEntity patchById(@PathVariable("customerId")UUID customerId,@RequestBody CustomerDTO customer){
        if(customerService.patchCustomerById(customerId,customer).isEmpty()){
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
