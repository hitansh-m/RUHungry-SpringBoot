package com.ruhungry.service;

import com.ruhungry.core.*;
import com.ruhungry.dto.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantService {
    
    private RUHungry restaurant;

    @PostConstruct
    public void initialize() throws IOException {
        restaurant = new RUHungry();
        
        // Load data from resources folder
        ClassPathResource stockResource = new ClassPathResource("data/stock.in");
        ClassPathResource menuResource = new ClassPathResource("data/menu.in");
        ClassPathResource tablesResource = new ClassPathResource("data/tables1.in");
        
        restaurant.createStockHashTable(stockResource.getInputStream());
        restaurant.menu(menuResource.getInputStream());
        restaurant.createTables(tablesResource.getInputStream());
        restaurant.updatePriceAndProfit();
    }

    public List<DishDTO> getAllDishes() {
        List<DishDTO> dishes = new ArrayList<>();
        MenuNode[] menu = restaurant.getMenu();
        
        if (menu == null) return dishes;
        
        for (int i = 0; i < menu.length; i++) {
            MenuNode node = menu[i];
            while (node != null) {
                Dish dish = node.getDish();
                dishes.add(new DishDTO(
                    dish.getName(),
                    dish.getCategory(),
                    dish.getPriceOfDish(),
                    dish.getProfit()
                ));
                node = node.getNextMenuNode();
            }
        }
        return dishes;
    }

    public List<DishDTO> getDishesByCategory(String category) {
        List<DishDTO> dishes = new ArrayList<>();
        MenuNode[] menu = restaurant.getMenu();
        String[] categories = restaurant.getCategoryArray();
        
        if (menu == null || categories == null) return dishes;
        
        for (int i = 0; i < categories.length; i++) {
            if (categories[i].equalsIgnoreCase(category)) {
                MenuNode node = menu[i];
                while (node != null) {
                    Dish dish = node.getDish();
                    dishes.add(new DishDTO(
                        dish.getName(),
                        dish.getCategory(),
                        dish.getPriceOfDish(),
                        dish.getProfit()
                    ));
                    node = node.getNextMenuNode();
                }
                break;
            }
        }
        return dishes;
    }

    public String[] getCategories() {
        return restaurant.getCategoryArray();
    }

    public OrderResponse placeOrder(String dishName, int quantity) {
        boolean wasAvailable = restaurant.checkDishAvailability(dishName, quantity);
        restaurant.order(dishName, quantity);
        
        return new OrderResponse(
            dishName,
            quantity,
            wasAvailable,
            restaurant.profit()
        );
    }

    public StockResponse getStock(String ingredientName) {
        StockNode node = restaurant.findStockNode(ingredientName);
        if (node != null) {
            Ingredient ing = node.getIngredient();
            return new StockResponse(
                ing.getID(),
                ing.getName(),
                ing.getStockLevel(),
                ing.getCost()
            );
        }
        return null;
    }

    public void updateStock(String ingredientName, int amount) {
        restaurant.updateStock(ingredientName, -1, amount);
    }

    public void restockIngredient(String ingredientName, int quantity) {
        restaurant.restock(ingredientName, quantity);
    }

    public void donateIngredient(String ingredientName, int quantity) {
        restaurant.donation(ingredientName, quantity);
    }

    public double getCurrentProfit() {
        return restaurant.profit();
    }

    public List<String> getAllIngredients() {
        List<String> ingredients = new ArrayList<>();
        StockNode[] stock = restaurant.getStockVar();
        
        if (stock == null) return ingredients;
        
        for (int i = 0; i < stock.length; i++) {
            StockNode node = stock[i];
            while (node != null) {
                ingredients.add(node.getIngredient().getName());
                node = node.getNextStockNode();
            }
        }
        return ingredients;
    }
}
