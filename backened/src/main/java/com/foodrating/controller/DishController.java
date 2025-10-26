package com.foodrating.controller;

import com.foodrating.entity.Dish;
import com.foodrating.repository.DishRepository;
import com.foodrating.repository.CanteenRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/dishes")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:8081"})
public class DishController {
    
    @Autowired
    private DishRepository dishRepository;
    
    @Autowired
    private CanteenRepository canteenRepository;
    
    @GetMapping
    public ResponseEntity<List<Dish>> getAllDishes() {
        try {
            List<Dish> dishes = dishRepository.findAll();
            return ResponseEntity.ok(dishes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Dish> getDishById(@PathVariable Long id) {
        Optional<Dish> dish = dishRepository.findById(id);
        return dish.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/canteen/{canteenId}")
    public ResponseEntity<List<Dish>> getDishesByCanteen(@PathVariable Long canteenId) {
        try {
            List<Dish> dishes = dishRepository.findByCanteenId(canteenId);
            return ResponseEntity.ok(dishes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // POST - Create new dish
    @PostMapping
    public ResponseEntity<?> createDish(@RequestBody Dish dish) {
        try {
            System.out.println("Received dish: " + dish); // Debug log
            
            // Validate required fields
            if (dish.getName() == null || dish.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Dish name is required");
            }
            
            if (dish.getCanteen() == null || dish.getCanteen().getId() == null) {
                return ResponseEntity.badRequest().body("Canteen ID is required");
            }
            
            // Check if canteen exists
            if (!canteenRepository.existsById(dish.getCanteen().getId())) {
                return ResponseEntity.badRequest().body("Canteen not found with ID: " + dish.getCanteen().getId());
            }
            
            // Set default values if null
            if (dish.getPrice() == null) dish.setPrice(0.0);
            if (dish.getRating() == null) dish.setRating(1);
            
            Dish savedDish = dishRepository.save(dish);
            System.out.println("Saved dish: " + savedDish); // Debug log
            
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDish);
            
        } catch (Exception e) {
            System.out.println("Error creating dish: " + e.getMessage()); // Debug log
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body("Error creating dish: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDish(@PathVariable Long id, @Valid @RequestBody Dish dishDetails, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        
        try {
            Optional<Dish> optionalDish = dishRepository.findById(id);
            if (optionalDish.isPresent()) {
                Dish dish = optionalDish.get();
                dish.setName(dishDetails.getName());
                dish.setDescription(dishDetails.getDescription());
                dish.setCategory(dishDetails.getCategory());
                dish.setPrice(dishDetails.getPrice());
                dish.setRating(dishDetails.getRating());
                dish.setAvailableDate(dishDetails.getAvailableDate());
                
                Dish updatedDish = dishRepository.save(dish);
                return ResponseEntity.ok(updatedDish);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Error updating dish: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDish(@PathVariable Long id) {
        try {
            if (dishRepository.existsById(id)) {
                dishRepository.deleteById(id);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body("Error deleting dish: " + e.getMessage());
        }
    }
}