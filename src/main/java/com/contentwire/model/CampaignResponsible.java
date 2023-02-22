package com.contentwire.model;

/**
 * A Campaign Manager that manages E-Mail Marketing Campaigns assigned to them. They are responsible for writing and sending E-Mails for the audiences within their campaigns.
 */

public class CampaignResponsible extends CampaignManager {
    public CampaignResponsible(String userName, String passwordHash) {
        super(userName, passwordHash, DisplayMode.RESPONSIBLE);
    }
}
