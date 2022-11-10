package com.example.dogManager.service;

import com.example.dogManager.exceptions.DogNotFoundException;
import com.example.dogManager.model.Dog;
import com.example.dogManager.model.DogUrl;
import com.example.dogManager.repository.DogManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Service
public class DogManagerService {

    private final DogManagerRepository dogManagerRepository;

    @Autowired
    public DogManagerService(DogManagerRepository dogManagerRepository) {
        this.dogManagerRepository = dogManagerRepository;
    }

    /**
     * Method to return all the dogs in db
     * @return List<Dog>
     */
    public List<Dog> findAllProducts() {
        return dogManagerRepository.findAll();
    }

    /**
     * Method to return a Product with ID as filter
     * @param id
     * @return Dog
     * @throws DogNotFoundException
     */
    private Dog findProductById(Long id) throws DogNotFoundException {
        Dog dog = dogManagerRepository.findDogById(id);
        if(dog == null) {
            throw new DogNotFoundException("there no dog with the id: " + id);
        }
        return dog;
    }

    /**
     * Method to add a dog to the db
     * @param dog
     * @return Dog
     */
    public Dog addProduct(DogUrl dog) {
        Dog newProduct = new Dog(dog.getName(),
                dog.getDescription(),
                getImageFromNetByUrl(dog.getImage()),
                dog.getStorageCount());
        //using the dog repository layer to add the dog to the db without repeating queries
        return dogManagerRepository.save(newProduct);
    }

    /**
     * Method to delete a Product with ID as filter
     * @param id
     */
    public void deleteProduct(Long id) {
        try {
            findProductById(id);
            dogManagerRepository.deleteDogById(id);
        } catch (DogNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Converts the Image Url into a array of bytes, by doing a connection to the img url
     * @param strUrl
     * @return byte[] btImg
     */
    private byte[] getImageFromNetByUrl(String strUrl) {
        try {
            if(strUrl == null) return null;
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();
            byte[] btImg = readInputStream(inStream);
            return btImg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * read and converts the input stream into a array of bytes
     * @param inStream
     * @return ByteArrayOutputStream outStream
     * @throws IOException
     */
    private byte[] readInputStream(InputStream inStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[10240];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();

    }
}
