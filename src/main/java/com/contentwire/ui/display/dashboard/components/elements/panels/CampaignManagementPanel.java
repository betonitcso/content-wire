package com.contentwire.ui.display.dashboard.components.elements.panels;

import com.contentwire.model.Campaign;
import com.contentwire.service.repository.service.ConnectionService;
import com.contentwire.ui.display.composer.CampaignComposerUI;
import com.contentwire.ui.display.dashboard.components.elements.buttons.QuickActionButton;
import com.contentwire.ui.display.dashboard.components.pages.orchestrator.pages.Campaigns;
import com.contentwire.ui.display.relation.manager.RelationManagerUI;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Styled JPanel to show actions for selected campaigns
 */

public class CampaignManagementPanel extends JPanel {

    private final Campaigns dashboard;

    private QuickActionButton deleteButton;
    private QuickActionButton manageAudiencesButton;
    private Campaign selectedCampaign;

    /**
     * Initialize Swing components, add callback buttons, style components
     * @param dashboard the dashboard the callback should arrive to
     */

    public CampaignManagementPanel(Campaigns dashboard) {

        this.dashboard = dashboard;

        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
            this.setLayout(layout);

        JLabel sectionTitle = new JLabel("Quick Actions");
            sectionTitle.putClientProperty( "FlatLaf.styleClass", "h1" );

        JLabel actionTitle = new JLabel("Manage Campaign");
            actionTitle.putClientProperty( "FlatLaf.styleClass", "h1" );

            this.add(Box.createVerticalStrut(20));
            this.add(sectionTitle);
            this.add(Box.createVerticalStrut(10));

        String plusIconUrl = "src/main/resources/assets/icons/plus-square.png";
            this.add(new QuickActionButton("Add Campaign", plusIconUrl, () -> {
            CampaignComposerUI ui = new CampaignComposerUI(dashboard);
            ui.setVisible(true);
        }));
            this.add(Box.createVerticalStrut(40));

            this.add(actionTitle);
            this.add(Box.createVerticalStrut(10));

        String trashIconUrl = "src/main/resources/assets/icons/trash-03.png";

        deleteButton = new QuickActionButton("Delete", trashIconUrl, this::deleteSelectedCampaign);
            deleteButton.setBackground(new Color(255, 71, 71, 92));

        manageAudiencesButton = new QuickActionButton("Manage Audiences", () -> {

            Map<String, Boolean> audienceRelationList = new HashMap<>();

            List<String> audienceList = new ArrayList<>();
            try {
                ConnectionService.getAudiencesForCampaign(selectedCampaign.getName()).forEach(i -> {
                    audienceRelationList.put(i.getName(), true);
                    audienceList.add(i.getName());
                });
                ConnectionService.getAudiences().forEach(i -> { if(audienceList.stream().noneMatch(x -> x.equals(i.getName()))) {
                    audienceRelationList.put(i.getName(), false);
                }

                });
            } catch (SQLException ignored) { ignored.printStackTrace();}

            RelationManagerUI ui = new RelationManagerUI("Audiences", audienceRelationList, this::deleteAudienceConnection, this::addAudienceConnection);
            ui.setVisible(true);

        });

        manageAudiencesButton.setEnabled(false);
        deleteButton.setEnabled(false);

        this.add(Box.createVerticalStrut(10));
        this.add(manageAudiencesButton);
        this.add(Box.createVerticalStrut(10));
        this.add(deleteButton);
    }

    /**
     * Insert a new audience - campaign connection.
     * @param audience the audience of the connection
     */

    private void addAudienceConnection(String audience) {
        try {
            ConnectionService.setCampaignAudience(selectedCampaign.getName(), audience);
        } catch (SQLException e) { e.printStackTrace();}
    }

    /**
     * Remove an Audience - Campaign connection.
     * @param audience the audience of the connection
     */
    private void deleteAudienceConnection(String audience) {
        try {
            ConnectionService.removeCampaignAudience(selectedCampaign.getName(), audience);
        } catch (SQLException ignored) { }
    }

    /**
     * Remove the selected campaign from the database.
     */
    private void deleteSelectedCampaign() {
        try {
            ConnectionService.deleteCampaign(this.selectedCampaign.getName());
            deleteButton.setEnabled(false);
            manageAudiencesButton.setEnabled(false);

        } catch (SQLException ignored) { }
        this.dashboard.refreshContent();
    }

    /**
     * Enable quick action buttons for a selected campaign.
     * @param campaign campaign to enable actions for
     */

    public void showUserQuickActions(Campaign campaign) {
        this.selectedCampaign = campaign;
        deleteButton.setEnabled(true);
        manageAudiencesButton.setEnabled(true);
    }
}
