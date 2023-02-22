package com.contentwire.model;

/**
 * Model representing employees that participate in E-Mail Marketing workflows.
 */


public class CampaignManager {
    private DisplayMode displayMode;
    private final String userName;
    private String passwordHash;
    private boolean isAuthenticated = false;

    public CampaignManager(String userName, String passwordHash) {
        this.userName = userName;
        this.passwordHash = passwordHash;
    }

    public CampaignManager(String userName, String passwordHash, DisplayMode displayMode) {
        this(userName, passwordHash);
        this.displayMode = displayMode;
    }

    public CampaignManager(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    public DisplayMode getDisplayMode() {
        return displayMode;
    }
}
