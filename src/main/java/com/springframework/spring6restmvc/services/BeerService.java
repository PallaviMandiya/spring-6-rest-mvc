package com.springframework.spring6restmvc.services;

import com.springframework.spring6restmvc.model.Beer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    List<Beer> beerList();

    Optional<Beer> getBeerById(UUID id);

    Beer saveNewBeer(Beer beer);

    void updateBeerById(UUID beerId, Beer beer);

    void deleteBeerById(UUID beerId);

    void patchBeerById(UUID beerId, Beer beer);
}
