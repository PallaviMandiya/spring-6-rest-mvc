package com.springframework.spring6restmvc.services;

import com.springframework.spring6restmvc.model.Beer;
import com.springframework.spring6restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    private Map<UUID, Beer> beerMap;

    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();

        Beer beer1 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Cranky")
                .beerStyle(BeerStyle.LAGER)
                .upa("246313")
                .price(new BigDecimal(13.43))
                .quantityOnHand(152)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer2 = Beer.builder()
                .id(UUID.randomUUID())
                .version(2)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upa("246211")
                .price(new BigDecimal(12.99))
                .quantityOnHand(122)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer3 = Beer.builder()
                .id(UUID.randomUUID())
                .version(3)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.IPA)
                .upa("92783")
                .price(new BigDecimal(13.99))
                .quantityOnHand(423)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerMap.put(beer1.getId(), beer1);
        beerMap.put(beer2.getId(),beer2);
        beerMap.put(beer3.getId(),beer3);
    }

    @Override
    public List<Beer> beerList(){
            return new ArrayList<>(beerMap.values());
    }

    @Override
    public Optional<Beer> getBeerById(UUID id) {
        log.debug("Beer By Id - from Service Called - Beer Id "+id.toString());
        return Optional.of(beerMap.get(id));
    }

    @Override
    public Beer saveNewBeer(Beer beer) {
        Beer savedBeer =  Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName(beer.getBeerName())
                .beerStyle(beer.getBeerStyle())
                .upa(beer.getUpa())
                .price(beer.getPrice())
                .quantityOnHand(beer.getQuantityOnHand())
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        beerMap.put(savedBeer.getId(), savedBeer);
        System.out.println("Beer: "+savedBeer);
        return savedBeer;
    }

    @Override
    public void updateBeerById(UUID beerId, Beer beer) {
        Beer existing = beerMap.get(beerId);
        existing.setBeerName(beer.getBeerName());
        existing.setBeerStyle(beer.getBeerStyle());
        existing.setPrice(beer.getPrice());
        existing.setVersion(beer.getVersion());
        existing.setUpa(beer.getUpa());

//        beerMap.put(existing.getId(),existing);
    }

    @Override
    public void deleteBeerById(UUID beerId) {
        beerMap.remove(beerId);
    }

    @Override
    public void patchBeerById(UUID beerId, Beer beer) {
        Beer existing = beerMap.get(beerId);

        if(StringUtils.hasText(beer.getBeerName())){
            existing.setBeerName(beer.getBeerName());
        }

        if(beer.getBeerStyle() != null){
            existing.setBeerStyle(beer.getBeerStyle());
        }

        if(beer.getPrice() != null){
            existing.setPrice(beer.getPrice());
        }

        if(beer.getQuantityOnHand() != null){
            existing.setQuantityOnHand(beer.getQuantityOnHand());
        }

        if(StringUtils.hasText(beer.getUpa())){
            existing.setUpa(beer.getUpa());
        }
    }

//    @Override
//    public Beer getBeerById(UUID id) {
//        log.debug("Beer By Id - from Service Called - Beer Id "+id.toString());
//        return Beer.builder()
//                .id(id)
//                .version(1)
//                .beerName("Galaxy Cat")
//                .beerStyle(BeerStyle.PALE_ALE)
//                .upa("246211")
//                .price(new BigDecimal(12.99))
//                .quantityOnHand(122)
//                .createDate(LocalDateTime.now())
//                .updateDate(LocalDateTime.now())
//                .build();
//    }


}
