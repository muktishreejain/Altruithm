package com.example.demo1.model;

import java.util.Objects;

public class Charity {
    private String name;
    private String category;
    private String description;

    // Required empty constructor for Gson
    public Charity() {}

    public Charity(String name, String category, String description) {
        this.name = name;
        this.category = category;
        this.description = description;
    }

    // Getters
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setDescription(String description) { this.description = description; }

    // Optional: Useful for debugging or displaying in UI
    @Override
    public String toString() {
        String safeName = Objects.toString(name, "");
        String safeCategory = Objects.toString(category, "");
        String safeDescription = Objects.toString(description, "");
        return safeName + " (" + safeCategory + "): " + safeDescription;
    }
}
