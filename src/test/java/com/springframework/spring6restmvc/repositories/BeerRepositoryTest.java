package com.springframework.spring6restmvc.repositories;

import com.springframework.spring6restmvc.bootstrap.BootstrapData;
import com.springframework.spring6restmvc.entities.Beer;
import com.springframework.spring6restmvc.model.BeerStyle;
import com.springframework.spring6restmvc.services.BeerCsvService;
import com.springframework.spring6restmvc.services.BeerCsvServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({BootstrapData.class, BeerCsvServiceImpl.class})
class BeerRepositoryTest {
    @Autowired
    BeerRepository beerRepository;


    @Test
    void testGetBeerListByName() {
        List<Beer> list = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA%");
        assertThat(list.size()).isEqualTo(30);
    }

    @Test
    void testSaveBeerNameTooLong() {
        assertThrows(ConstraintViolationException.class, () ->{
            Beer saveBeer = beerRepository.save(Beer.builder()
                    .beerName("My Beer 123456789012345678901234567890123456789012345678901234567890")
                    .beerStyle(BeerStyle.PALE_ALE)
                    .upa("23343")
                    .price(new BigDecimal(12.23))
                    .build());
            beerRepository.flush();
//            assertThat(saveBeer).isNotNull();
//            assertThat(saveBeer.getId()).isNotNull();
        });
        
        

    }

    @Test
    void testSaveBeer() {
        Beer saveBeer = beerRepository.save(Beer.builder()
                    .beerName("My Beer")
                    .beerStyle(BeerStyle.PALE_ALE)
                    .upa("23343")
                    .price(new BigDecimal(12.23))
                .build());
        beerRepository.flush();
        assertThat(saveBeer).isNotNull();
        assertThat(saveBeer.getId()).isNotNull();
    }
}