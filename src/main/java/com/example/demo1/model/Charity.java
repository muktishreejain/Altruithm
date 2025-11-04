package com.example.demo1.model;

public class Charity {
    private String name;
    private String category;
    private String description;

    public Charity() {}

    public Charity(String name, String category, String description) {
        this.name = name;
        this.category = category;
        this.description = description;
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }

    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setDescription(String description) { this.description = description; }
}

