package com.ruhungry.dto;

import java.util.List;

public class RecipeDTO {
    private String dishName;
    private List<IngredientInfo> ingredients;

    public RecipeDTO() {}

    public RecipeDTO(String dishName, List<IngredientInfo> ingredients) {
        this.dishName = dishName;
        this.ingredients = ingredients;
    }

    // Getters and setters
    public String getDishName() { return dishName; }
    public void setDishName(String dishName) { this.dishName = dishName; }

    public List<IngredientInfo> getIngredients() { return ingredients; }
    public void setIngredients(List<IngredientInfo> ingredients) { this.ingredients = ingredients; }

    // Inner class for ingredient info
    public static class IngredientInfo {
        private int id;
        private String name;
        private int quantityPerDish;
        private int currentStock;
        private double cost;

        public IngredientInfo() {}

        public IngredientInfo(int id, String name, int quantityPerDish, int currentStock, double cost) {
            this.id = id;
            this.name = name;
            this.quantityPerDish = quantityPerDish;
            this.currentStock = currentStock;
            this.cost = cost;
        }

        // Getters and setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public int getQuantityPerDish() { return quantityPerDish; }
        public void setQuantityPerDish(int quantityPerDish) { this.quantityPerDish = quantityPerDish; }

        public int getCurrentStock() { return currentStock; }
        public void setCurrentStock(int currentStock) { this.currentStock = currentStock; }

        public double getCost() { return cost; }
        public void setCost(double cost) { this.cost = cost; }
    }
}
