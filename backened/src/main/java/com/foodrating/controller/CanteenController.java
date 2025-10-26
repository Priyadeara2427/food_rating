package com.foodrating.controller;

import com.foodrating.entity.Canteen;
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
@RequestMapping("/api/canteens")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:8081"})
public class CanteenController {
    
    @Autowired
    private CanteenRepository canteenRepository;
    
    @GetMapping
    public ResponseEntity<List<Canteen>> getAllCanteens() {
        try {
            List<Canteen> canteens = canteenRepository.findAll();
            return ResponseEntity.ok(canteens);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Canteen> getCanteenById(@PathVariable Long id) {
        Optional<Canteen> canteen = canteenRepository.findById(id);
        return canteen.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<?> createCanteen(@Valid @RequestBody Canteen canteen, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        
        if (canteenRepository.existsByName(canteen.getName())) {
            return ResponseEntity.badRequest().body("Canteen with this name already exists");
        }
        
        try {
            Canteen savedCanteen = canteenRepository.save(canteen);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCanteen);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body("Error creating canteen: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCanteen(@PathVariable Long id, @Valid @RequestBody Canteen canteenDetails, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        
        try {
            Optional<Canteen> optionalCanteen = canteenRepository.findById(id);
            if (optionalCanteen.isPresent()) {
                Canteen canteen = optionalCanteen.get();
                
                // Check if name is being changed and if it conflicts with existing
                if (!canteen.getName().equals(canteenDetails.getName()) && 
                    canteenRepository.existsByName(canteenDetails.getName())) {
                    return ResponseEntity.badRequest().body("Canteen with this name already exists");
                }
                
                canteen.setName(canteenDetails.getName());
                canteen.setLocation(canteenDetails.getLocation());
                canteen.setDescription(canteenDetails.getDescription());
                
                Canteen updatedCanteen = canteenRepository.save(canteen);
                return ResponseEntity.ok(updatedCanteen);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body("Error updating canteen: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCanteen(@PathVariable Long id) {
        try {
            if (canteenRepository.existsById(id)) {
                canteenRepository.deleteById(id);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body("Error deleting canteen: " + e.getMessage());
        }
    }
}