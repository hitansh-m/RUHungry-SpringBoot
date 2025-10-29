package com.ruhungry.controller;

import com.ruhungry.dto.DishDTO;
import com.ruhungry.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@CrossOrigin(origins = "*")
public class MenuController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<List<DishDTO>> getAllDishes() {
        return ResponseEntity.ok(restaurantService.getAllDishes());
    }

    @GetMapping("/categories")
    public ResponseEntity<String[]> getCategories() {
        return ResponseEntity.ok(restaurantService.getCategories());
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<DishDTO>> getDishesByCategory(@PathVariable String categoryName) {
        return ResponseEntity.ok(restaurantService.getDishesByCategory(categoryName));
    }
}
