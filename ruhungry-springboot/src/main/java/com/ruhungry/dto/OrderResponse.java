package com.ruhungry.dto;

public class OrderResponse {
    private String dishName;
    private int quantity;
    private boolean successful;
    private double currentProfit;

    public OrderResponse() {}

    public OrderResponse(String dishName, int quantity, boolean successful, double currentProfit) {
        this.dishName = dishName;
        this.quantity = quantity;
        this.successful = successful;
        this.currentProfit = currentProfit;
    }

    // Getters and setters
    public String getDishName() { return dishName; }
    public void setDishName(String dishName) { this.dishName = dishName; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public boolean isSuccessful() { return successful; }
    public void setSuccessful(boolean successful) { this.successful = successful; }
    
    public double getCurrentProfit() { return currentProfit; }
    public void setCurrentProfit(double currentProfit) { this.currentProfit = currentProfit; }
}
