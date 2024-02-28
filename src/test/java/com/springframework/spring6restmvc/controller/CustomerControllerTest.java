package com.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springframework.spring6restmvc.model.Beer;
import com.springframework.spring6restmvc.model.Customer;
import com.springframework.spring6restmvc.services.CustomerService;
import com.springframework.spring6restmvc.services.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    CustomerService customerService;
    @Autowired
    ObjectMapper objectMapper;

    CustomerServiceImpl customerServiceImpl;

    @BeforeEach
    void setUp() {
        customerServiceImpl = new CustomerServiceImpl();
    }
    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;
    @Captor
    ArgumentCaptor<Customer> customerArgumentCaptor;

    @Test
    void getCustomerById() throws Exception {
//        System.out.println(customerController.getCustomerById(UUID.randomUUID()));

        Customer testCustomer = customerServiceImpl.listCustomer().get(0);
//        given(customerService.getCustomerById(any(UUID.class))).willReturn(testCustomer);

        given(customerService.getCustomerById(testCustomer.getId())).willReturn(Optional.of(testCustomer));
//        mockMvc.perform(get("/api/v1/customer/"+UUID.randomUUID())
        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID,  testCustomer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id",is(testCustomer.getId().toString())))
                .andExpect(jsonPath("$.customerName",is(testCustomer.getCustomerName())));
    }

    @Test
    void testListCustomer() throws Exception {
        given(customerService.listCustomer()).willReturn(customerServiceImpl.listCustomer());
        mockMvc.perform(get(CustomerController.CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()",is(3)));
    }

    @Test
    void testCreateNewCustomer() throws Exception {
        Customer customer = customerServiceImpl.listCustomer().get(0);
        customer.setId(null);

        given(customerService.saveNewCustomer(any(Customer.class))).willReturn(customerServiceImpl.listCustomer().get(1));
        mockMvc.perform(post(CustomerController.CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                     .contentType(MediaType.APPLICATION_JSON)
                     .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testUpdateCustomerById() throws Exception {
        Customer customer = customerServiceImpl.listCustomer().get(0);
        mockMvc.perform(put(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)));

        verify(customerService).updateCustomerById(any(UUID.class),any(Customer.class));
    }

    @Test
    void testDeleteById() throws Exception {
        Customer customer = customerServiceImpl.listCustomer().get(0);
        mockMvc.perform(delete(CustomerController.CUSTOMER_PATH_ID,  customer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

//        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(customerService).deleteCustomerById(uuidArgumentCaptor.capture());
        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testPatchCustomer() throws Exception {
        Customer customer = customerServiceImpl.listCustomer().get(0);
        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("CustomerName", "NewName");

        mockMvc.perform(patch(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerMap)))
                .andExpect(status().isNoContent());

        verify(customerService).patchCustomerById(uuidArgumentCaptor.capture(),customerArgumentCaptor.capture());
        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(customerMap.get("CustomerName")).isEqualTo(customerArgumentCaptor.getValue().getCustomerName());

    }
    @Test
    void getCustomerByIdNotFount() throws Exception {
        given(customerService.getCustomerById(any(UUID.class))).willReturn(Optional.empty());
        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}