package com.ruhungry.dto;

public class RestockRequest {
    private String ingredientName;
    private int quantity;

    public RestockRequest() {}

    public RestockRequest(String ingredientName, int quantity) {
        this.ingredientName = ingredientName;
        this.quantity = quantity;
    }

    // Getters and setters
    public String getIngredientName() { return ingredientName; }
    public void setIngredientName(String ingredientName) { this.ingredientName = ingredientName; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
