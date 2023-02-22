package com.contentwire.model;

/**
 * A model for representing groups of audience members of E-Mail marketing campaigns.
 */

public class Audience {
    private String name;
    private String description;

    public Audience() {
        this(null, null);
    }

    public Audience(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
