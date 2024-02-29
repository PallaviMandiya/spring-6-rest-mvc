package com.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springframework.spring6restmvc.model.BeerDTO;
import com.springframework.spring6restmvc.services.BeerService;
import com.springframework.spring6restmvc.services.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.swing.text.html.Option;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.io.UnsupportedEncodingException;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

//@SpringBootTest
@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    BeerService beerService;

    @Autowired
    ObjectMapper objectMapper;

    BeerServiceImpl beerServiceImpl;

    @BeforeEach
    void setup() {
        beerServiceImpl = new BeerServiceImpl();
    }
    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;
    @Captor
    ArgumentCaptor<BeerDTO> beerArgumentCaptor;

    @Test
    void testGetByIdNotFount() throws Exception {
        given(beerService.getBeerById(any(UUID.class))).willThrow(NotFoundException.class);
        mockMvc.perform(get(BeerController.BEER_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBeerById() throws Exception{
//        System.out.println(beerController.getBeerById(UUID.randomUUID()));
        BeerDTO testBeer = beerServiceImpl.beerList().get(0);
//        given(beerService.getBeerById(any(UUID.class))).willReturn(testBeer);

        given(beerService.getBeerById(testBeer.getId())).willReturn(Optional.of(testBeer));

//        mockMvc.perform(get("/api/v1/beer/"+UUID.randomUUID())
        mockMvc.perform(get(BeerController.BEER_PATH_ID, testBeer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id",is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName",is(testBeer.getBeerName())));
    }

    @Test
    void testListBeer() throws Exception {
        given(beerService.beerList()).willReturn(beerServiceImpl.beerList());
        mockMvc.perform(get(BeerController.BEER_PATH)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()",is(3)));

    }
    @Test
    void testCreateNewBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.beerList().get(0);
        beer.setVersion(null);
        beer.setId(null);

        given(beerService.saveNewBeer(any(BeerDTO.class))).willReturn(beerServiceImpl.beerList().get(1));

        mockMvc.perform(post(BeerController.BEER_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testUpdateBeerById() throws Exception {
        BeerDTO beer = beerServiceImpl.beerList().get(0);
        given(beerService.updateBeerById(any(), any())).willReturn(Optional.of(beer));
        mockMvc.perform(put(BeerController.BEER_PATH_ID, beer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)));

        verify(beerService).updateBeerById(any(UUID.class),any(BeerDTO.class));
    }

    @Test
    void testUpdateBeerBlankName() throws Exception {
        BeerDTO beerDto = beerServiceImpl.beerList().get(0);
        beerDto.setBeerName(null);
        given(beerService.updateBeerById(any(), any())).willReturn(Optional.of(beerDto));
        MvcResult mvcResult = mockMvc.perform(put(BeerController.BEER_PATH_ID, beerDto.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerDto)))
                .andExpect(status().isBadRequest())
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testDeleteById() throws Exception {
        BeerDTO beer = beerServiceImpl.beerList().get(0);
        given(beerService.deleteBeerById(any())).willReturn(true);
        mockMvc.perform(delete(BeerController.BEER_PATH_ID, beer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(beerService).deleteBeerById(uuidArgumentCaptor.capture());
        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testPatchBeer() throws Exception {
        BeerDTO beer =beerServiceImpl.beerList().get(0);
        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "NewBeer");
        mockMvc.perform(patch(BeerController.BEER_PATH_ID,beer.getId())
                .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap))
                .contentType(MediaType.APPLICATION_JSON));
        verify(beerService).patchBeerById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());
        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(beerMap.get("beerName")).isEqualTo(beerArgumentCaptor.getValue().getBeerName());
    }
    @Test
    void getBeerByIdNotFount() throws Exception {
        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());
        mockMvc.perform(get(BeerController.BEER_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateBeerNullName() throws Exception {
        BeerDTO beer = BeerDTO.builder().build();

        MvcResult mvcResult = mockMvc.perform(post(BeerController.BEER_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isBadRequest())
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
}