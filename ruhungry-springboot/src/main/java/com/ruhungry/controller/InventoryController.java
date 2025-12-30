package com.ruhungry.controller;

import com.ruhungry.dto.*;
import com.ruhungry.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*")
public class InventoryController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<List<String>> getAllIngredients() {
        return ResponseEntity.ok(restaurantService.getAllIngredients());
    }

    @GetMapping("/{ingredientName}")
    public ResponseEntity<StockResponse> getStock(@PathVariable String ingredientName) {
        StockResponse stock = restaurantService.getStock(ingredientName);
        if (stock != null) {
            return ResponseEntity.ok(stock);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{ingredientName}")
    public ResponseEntity<Void> updateStock(
            @PathVariable String ingredientName,
            @RequestParam int amount) {
        restaurantService.updateStock(ingredientName, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/restock")
    public ResponseEntity<Void> restock(@RequestBody RestockRequest request) {
        restaurantService.restockIngredient(request.getIngredientName(), request.getQuantity());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/donate")
    public ResponseEntity<Void> donate(@RequestBody RestockRequest request) {
        restaurantService.donateIngredient(request.getIngredientName(), request.getQuantity());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> resetInventory() {
        try {
            restaurantService.resetInventory();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
