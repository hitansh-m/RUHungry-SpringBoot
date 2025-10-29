package com.ruhungry.controller;

import com.ruhungry.dto.*;
import com.ruhungry.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest request) {
        OrderResponse response = restaurantService.placeOrder(
            request.getDishName(),
            request.getQuantity()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profit")
    public ResponseEntity<Double> getCurrentProfit() {
        return ResponseEntity.ok(restaurantService.getCurrentProfit());
    }
}
