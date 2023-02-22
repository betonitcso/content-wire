package com.contentwire.model;

/**
 * A model for representing audience members targeted by an E-Mail Marketing campaign.
 */

public class AudienceMember {
    private String email;
    private String name;

    public AudienceMember(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
