package com.springframework.spring6restmvc.bootstrap;

import com.springframework.spring6restmvc.entities.Beer;
import com.springframework.spring6restmvc.entities.Customer;
import com.springframework.spring6restmvc.model.BeerDTO;
import com.springframework.spring6restmvc.model.BeerStyle;
import com.springframework.spring6restmvc.model.CustomerDTO;
import com.springframework.spring6restmvc.repositories.BeerRepository;
import com.springframework.spring6restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {
    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        loadBeerData();
        loadCustomerData();
    }
    private void loadBeerData() {
        if(beerRepository.count() == 0){
            Beer beer1 = Beer.builder()
                    .beerName("Cranky")
                    .beerStyle(BeerStyle.LAGER)
                    .upa("246313")
                    .price(new BigDecimal(13.43))
                    .quantityOnHand(152)
                    .createDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            Beer beer2 = Beer.builder()
                    .beerName("Galaxy Cat")
                    .beerStyle(BeerStyle.PALE_ALE)
                    .upa("246211")
                    .price(new BigDecimal(12.99))
                    .quantityOnHand(122)
                    .createDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            Beer beer3 = Beer.builder()
                    .beerName("Galaxy Cat")
                    .beerStyle(BeerStyle.IPA)
                    .upa("92783")
                    .price(new BigDecimal(13.99))
                    .quantityOnHand(423)
                    .createDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();
            beerRepository.save(beer1);
            beerRepository.save(beer2);
            beerRepository.save(beer3);
        }
    }
    private void loadCustomerData() {
        if(customerRepository.count() == 0){
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

            customerRepository.saveAll(Arrays.asList(customer1, customer2, customer3));
        }
    }
}
