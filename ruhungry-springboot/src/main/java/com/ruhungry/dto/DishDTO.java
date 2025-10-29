package com.ruhungry.dto;

public class DishDTO {
    private String name;
    private String category;
    private double price;
    private double profit;

    public DishDTO() {}

    public DishDTO(String name, String category, double price, double profit) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.profit = profit;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public double getProfit() { return profit; }
    public void setProfit(double profit) { this.profit = profit; }
}
