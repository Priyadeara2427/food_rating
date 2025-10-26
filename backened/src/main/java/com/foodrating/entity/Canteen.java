package com.foodrating.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "canteens")
public class Canteen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    private String location;
    private String description;
    
    @OneToMany(mappedBy = "canteen", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"canteen", "hibernateLazyInitializer"})
    private List<Dish> dishes = new ArrayList<>();
    
    // Default constructor
    public Canteen() {}
    
    // Parameterized constructor
    public Canteen(String name, String location, String description) {
        this.name = name;
        this.location = location;
        this.description = description;
    }
    
    // Getters and setters
    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { 
        this.id = id; 
    }
    
    public String getName() { 
        return name; 
    }
    
    public void setName(String name) { 
        this.name = name; 
    }
    
    public String getLocation() { 
        return location; 
    }
    
    public void setLocation(String location) { 
        this.location = location; 
    }
    
    public String getDescription() { 
        return description; 
    }
    
    public void setDescription(String description) { 
        this.description = description; 
    }
    
    public List<Dish> getDishes() { 
        return dishes; 
    }
    
    public void setDishes(List<Dish> dishes) { 
        this.dishes = dishes; 
    }
    
    // Helper method to add dish
    public void addDish(Dish dish) {
        dishes.add(dish);
        dish.setCanteen(this);
    }
}