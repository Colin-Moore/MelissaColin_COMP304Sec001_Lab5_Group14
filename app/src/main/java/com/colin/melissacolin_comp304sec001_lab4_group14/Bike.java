package com.colin.melissacolin_comp304sec001_lab4_group14;

public class Bike {

    public String name;

    public String brand;

    public String category;

    public double cost;

    //Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    //constructor
    public Bike(String name, String brand, String category, double cost){
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.cost = cost;
    }
}
