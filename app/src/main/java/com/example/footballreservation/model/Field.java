package com.example.footballreservation.model;

public class Field {
    private int id;
    private String name;
    private String type;
    private double pricePerHour;
    private boolean isAvailable;
    private String imageUrl;

    // Constructor with id
    public Field(int id, String name, String type, double pricePerHour, boolean isAvailable, String imageUrl) {
        this(name, type, pricePerHour, isAvailable, imageUrl);
        this.id = id;
    }

    // Constructor without id
    public Field(String name, String type, double pricePerHour, boolean isAvailable, String imageUrl) {
        this.name = name;
        this.type = type;
        this.pricePerHour = pricePerHour;
        this.isAvailable = isAvailable;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return String.format("Field{id=%d, name='%s', type='%s', pricePerHour=%.2f, isAvailable=%b, imageUrl='%s'}",
                id, name, type, pricePerHour, isAvailable, imageUrl);
    }
}