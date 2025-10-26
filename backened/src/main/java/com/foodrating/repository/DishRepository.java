package com.foodrating.repository;

import com.foodrating.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByCanteenId(Long canteenId);
    List<Dish> findByCategory(String category);
    List<Dish> findByRatingGreaterThanEqual(Integer minRating);
}