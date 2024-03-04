package com.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springframework.spring6restmvc.entities.Beer;
import com.springframework.spring6restmvc.mapper.BeerMapper;
import com.springframework.spring6restmvc.model.BeerDTO;
import com.springframework.spring6restmvc.model.BeerStyle;
import com.springframework.spring6restmvc.repositories.BeerRepository;
import jakarta.transaction.Transactional;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
class BeerControllerIT {
    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerController beerController;

    @Autowired
    BeerMapper beerMapper;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void testQueryBeerByName() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                    .queryParam("beerName", "IPA")
                    .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()",is(336)));
    }

    @Test
    void testQueryBeerByBeerStyle() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                    .queryParam("beerStyle", BeerStyle.IPA.name())
                    .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()",is(548)));
    }

    @Test
    void testQueryBeerByNameAndStyle() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                    .queryParam("beerName", "IPA")
                    .queryParam("beerStyle", BeerStyle.IPA.name())
                    .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()",is(336)));
    }

    @Test
    void testListBeerByNameAndStyleShowInventoryTrue() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName","IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "true")
                        .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(336)))
                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.notNullValue()));
    }

    @Test
    void testListBeerByNameAndStyleShowInventoryFalse() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                    .queryParam("beerName","IPA")
                    .queryParam("beerStyle", BeerStyle.IPA.name())
                    .queryParam("showInventory", "false")
                    .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(336)))
                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.nullValue()));
    }

    @Test
    void tesListBeersByStyleAndNameShowInventoryTruePage2() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "true")
                        .queryParam("pageNumber", "2")
                        .queryParam("pageSize", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(50)))
                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.notNullValue()));
    }

    @Test
    void testPatchBeer() throws Exception {
        Beer beer = beerRepository.findAll().get(0);
        Map<String, Object> beerMap = new HashMap<>();

        beerMap.put("beerName", "NewBeer 1234567891234567890012345678901234567890123456789012345678901234567890");
        mockMvc.perform(patch(BeerController.BEER_PATH_ID,beer.getId())
                .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(beerMap))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()",is(1)));
    }

    @Test
    void testListBeer() {
        Page<BeerDTO> dtos = beerController.listBeer(null, null, false, 1, 2413);
        assertThat(dtos.getContent().size()).isEqualTo(1000);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        beerRepository.deleteAll();
        Page<BeerDTO> dtos = beerController.listBeer(null, null, false, 1, 25);
        assertThat(dtos.getContent().size()).isEqualTo(0);
    }

    @Test
    void testBeerIdNotFound() {
        assertThrows(NotFoundException.class,() ->{
            beerController.getBeerById(UUID.randomUUID());
        });

    }

    @Test
    void testGetBeerById() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO beerDTO = beerController.getBeerById(beer.getId());
        assertThat(beerDTO).isNotNull();
    }

    @Transactional
    @Rollback
    @Test
    void saveNewBeerTest() {
        BeerDTO beerDTO = BeerDTO.builder()
                .beerName("NewBeer")
                .build();
        ResponseEntity responseEntity = beerController.handlePost(beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);
        Beer beer = beerRepository.findById(savedUUID).get();
        assertThat(beer).isNotNull();

    }

    @Transactional
    @Rollback
    @Test
    void updateExistingBeer() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO beerDTO = beerMapper.beerToBeerDto(beer);
        beerDTO.setId(null);
        beerDTO.setVersion(null);
        final String beerName = "Updated";
        beerDTO.setBeerName(beerName);

        ResponseEntity responseEntity = beerController.updateById(beer.getId(),beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Beer updateBeer = beerRepository.findById(beer.getId()).get();
        assertThat(updateBeer.getBeerName()).isEqualTo(beerName);
    }

    @Test
    void testUpdateNotFound() {
        assertThrows(NotFoundException.class, () ->{
           beerController.updateById(UUID.randomUUID(), BeerDTO.builder().build());
        });
    }

    @Transactional
    @Rollback
    @Test
    void testDeleteByIdFound() {
        Beer beer = beerRepository.findAll().get(0);
        ResponseEntity responseEntity = beerController.deleteById(beer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(beerRepository.findById(beer.getId()).isEmpty());

//        Beer foundBeer = beerRepository.findById(beer.getId()).get();
//        assertThat(foundBeer).isNull();
    }

    @Test
    void testDeleteByIdNotFound() {
        assertThrows(NotFoundException.class, () ->{
            beerController.deleteById(UUID.randomUUID());
        });
    }

    @Transactional
    @Rollback
    @Test
    void patchExistingBeer() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO beerDTO = beerMapper.beerToBeerDto(beer);
        beerDTO.setId(null);
        beerDTO.setVersion(null);
        final String beerName = "Patched";
        beerDTO.setBeerName(beerName);

        ResponseEntity responseEntity = beerController.patchByID(beer.getId(),beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Beer patchBeer = beerRepository.findById(beer.getId()).get();
        assertThat(patchBeer.getBeerName()).isEqualTo(beerName);
    }

    @Test
    void testPatchByIdNotFound() {
        assertThrows(NotFoundException.class, () ->{
            beerController.patchByID(UUID.randomUUID(), BeerDTO.builder().build());
        });
    }
}