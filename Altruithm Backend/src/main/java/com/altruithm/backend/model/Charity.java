package com.altruithm.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "charities")
public class Charity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;
    private String website;
    private String contact;

    // Constructors
    public Charity() {}

    public Charity(String name, String description, String website, String contact) {
        this.name = name;
        this.description = description;
        this.website = website;
        this.contact = contact;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
}