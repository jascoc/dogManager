package com.example.dogManager.repository;

import com.example.dogManager.model.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//passing the metadata and the primary key type to the JpaRepository args
public interface DogManagerRepository extends JpaRepository<Dog, Long> {
    Dog findDogById(Long id);
    void deleteDogById(Long id);
}
