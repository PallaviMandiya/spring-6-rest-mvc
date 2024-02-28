package com.springframework.spring6restmvc.controller;

import com.springframework.spring6restmvc.model.Beer;
import com.springframework.spring6restmvc.services.BeerService;
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
@RestController
public class BeerController {
    public static final String BEER_PATH = "/api/v1/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    private final BeerService beerService;

    @GetMapping(BEER_PATH)
    public List<Beer> listBeer(){
        return beerService.beerList();
    }

    @GetMapping(BEER_PATH_ID)
        public Beer getBeerById(@PathVariable("beerId") UUID beerId){
            log.debug("Get Beer By Id - From Controller Called");
        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }
    @PostMapping(BEER_PATH)
    public ResponseEntity handlePost(@RequestBody Beer beer){

        Beer savedBeer = beerService.saveNewBeer(beer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", BEER_PATH + "/" + savedBeer.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping(BEER_PATH_ID)
    public ResponseEntity updateById(@PathVariable("beerId")UUID beerId, @RequestBody Beer beer) {
        beerService.updateBeerById(beerId,beer);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping(BEER_PATH_ID)
    public ResponseEntity deleteById(@PathVariable("beerId") UUID beerId){
        beerService.deleteBeerById(beerId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(BEER_PATH_ID)
    public ResponseEntity patchByID(@PathVariable("beerId")UUID beerId, @RequestBody Beer beer){
        beerService.patchBeerById(beerId,beer);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}
