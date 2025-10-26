package com.foodrating.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "dishes")
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String description;
    private String category;
    private Double price;
    private Integer rating;
    private LocalDate availableDate = LocalDate.now();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canteen_id")
    @JsonIgnoreProperties({"dishes", "hibernateLazyInitializer"})
    private Canteen canteen;
    
    // Default constructor
    public Dish() {}
    
    // Parameterized constructor
    public Dish(String name, String description, String category, Double price, Integer rating) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.rating = rating;
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
    
    public String getDescription() { 
        return description; 
    }
    
    public void setDescription(String description) { 
        this.description = description; 
    }
    
    public String getCategory() { 
        return category; 
    }
    
    public void setCategory(String category) { 
        this.category = category; 
    }
    
    public Double getPrice() { 
        return price; 
    }
    
    public void setPrice(Double price) { 
        this.price = price; 
    }
    
    public Integer getRating() { 
        return rating; 
    }
    
    public void setRating(Integer rating) { 
        this.rating = rating; 
    }
    
    public LocalDate getAvailableDate() { 
        return availableDate; 
    }
    
    public void setAvailableDate(LocalDate availableDate) { 
        this.availableDate = availableDate; 
    }
    
    public Canteen getCanteen() { 
        return canteen; 
    }
    
    public void setCanteen(Canteen canteen) { 
        this.canteen = canteen; 
    }
}