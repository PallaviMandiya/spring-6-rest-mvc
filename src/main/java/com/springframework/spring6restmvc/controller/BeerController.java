package com.springframework.spring6restmvc.controller;

import com.springframework.spring6restmvc.model.BeerDTO;
import com.springframework.spring6restmvc.model.BeerStyle;
import com.springframework.spring6restmvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
public class BeerController {
    public static final String BEER_PATH = "/api/v1/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    private final BeerService beerService;

    @GetMapping(BEER_PATH)
    public Page<BeerDTO> listBeer(@RequestParam(required = false) String beerName,
                                  @RequestParam(required = false) BeerStyle beerStyle,
                                  @RequestParam(required = false) Boolean showInventory,
                                  @RequestParam(required = false) Integer pageNumber,
                                  @RequestParam(required = false) Integer pageSize){

        return beerService.beerList(beerName,beerStyle, showInventory, pageNumber, pageSize);
    }

    @GetMapping(BEER_PATH_ID)
        public BeerDTO getBeerById(@PathVariable("beerId") UUID beerId){
            log.debug("Get Beer By Id - From Controller Called");
        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }
    @PostMapping(BEER_PATH)
    public ResponseEntity handlePost(@Validated @RequestBody BeerDTO beer){

        BeerDTO savedBeer = beerService.saveNewBeer(beer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", BEER_PATH + "/" + savedBeer.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping(BEER_PATH_ID)
    public ResponseEntity updateById(@PathVariable("beerId")UUID beerId,@Validated  @RequestBody BeerDTO beer) {
        if(beerService.updateBeerById(beerId,beer).isEmpty()){
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping(BEER_PATH_ID)
    public ResponseEntity deleteById(@PathVariable("beerId") UUID beerId){
        if(!beerService.deleteBeerById(beerId)){
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(BEER_PATH_ID)
    public ResponseEntity patchByID(@PathVariable("beerId")UUID beerId, @RequestBody BeerDTO beer){
        if(beerService.patchBeerById(beerId,beer).isEmpty()){
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}
