package com.foodrating.repository;

import com.foodrating.entity.Canteen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CanteenRepository extends JpaRepository<Canteen, Long> {
    List<Canteen> findByNameContainingIgnoreCase(String name);
    boolean existsByName(String name);
}