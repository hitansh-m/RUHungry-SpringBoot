package com.ruhungry.dto;

public class OrderRequest {
    private String dishName;
    private int quantity;

    public OrderRequest() {}

    public OrderRequest(String dishName, int quantity) {
        this.dishName = dishName;
        this.quantity = quantity;
    }

    // Getters and setters
    public String getDishName() { return dishName; }
    public void setDishName(String dishName) { this.dishName = dishName; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
