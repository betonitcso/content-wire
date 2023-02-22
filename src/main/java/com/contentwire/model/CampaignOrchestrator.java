package com.contentwire.model;

/**
 * A Campaign Manager that takes care of the structure of the organization and the E-Mail Marketing campaigns.
 */

public class CampaignOrchestrator extends CampaignManager {

    public CampaignOrchestrator(String userName, String passwordHash, boolean isRoot) {
        super(userName, passwordHash, DisplayMode.ORCHESTRATOR);
    }
}
