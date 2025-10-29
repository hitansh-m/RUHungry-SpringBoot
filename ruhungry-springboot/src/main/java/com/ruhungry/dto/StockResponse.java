package com.ruhungry.dto;

public class StockResponse {
    private int id;
    private String name;
    private int stockLevel;
    private double cost;

    public StockResponse() {}

    public StockResponse(int id, String name, int stockLevel, double cost) {
        this.id = id;
        this.name = name;
        this.stockLevel = stockLevel;
        this.cost = cost;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public int getStockLevel() { return stockLevel; }
    public void setStockLevel(int stockLevel) { this.stockLevel = stockLevel; }
    
    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }
}
