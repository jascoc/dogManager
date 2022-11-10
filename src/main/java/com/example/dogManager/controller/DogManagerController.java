package com.example.dogManager.controller;

import com.example.dogManager.model.Dog;
import com.example.dogManager.model.DogUrl;
import com.example.dogManager.service.DogManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/dogmanager")
public class DogManagerController {
    private final DogManagerService dogManagerService;

    @Autowired
    public DogManagerController(DogManagerService dogManagerService) {
        this.dogManagerService = dogManagerService;
    }

    /**
     * Controller layer: return all the dogs in the database
     * @return
     */
    @GetMapping//on default path
    public ResponseEntity<List<Dog>> getAllProducts() {
        List<Dog> dogs = dogManagerService.findAllProducts();
        return new ResponseEntity<>(dogs, HttpStatus.OK);
    }

    /**
     * Controller layer: deletes a dog with ID as filter
     * @param id
     */
    @Transactional
    @DeleteMapping ("/delete/{id}")                        //to specify what type i am expecting to avoid data injections
    public ResponseEntity<?> deleteProducts(@PathVariable("id") Long id) {
        dogManagerService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Controller layer: adding a new dog in the database
     * @param dog
     * @return Dog
     */
    @PostMapping("/add")                        //i am expecting a json
    public ResponseEntity<Dog> addProducts(@RequestBody DogUrl dog) {
        Dog newProduct = dogManagerService.addProduct(dog);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }
}
