package com.contentwire.ui.display.dashboard.components.elements.panels;

import com.contentwire.model.CampaignManager;
import com.contentwire.model.DisplayMode;
import com.contentwire.service.repository.service.ConnectionService;
import com.contentwire.ui.display.composer.UserComposerUI;
import com.contentwire.ui.display.dashboard.components.elements.buttons.QuickActionButton;
import com.contentwire.ui.display.dashboard.components.pages.orchestrator.pages.Organization;
import com.contentwire.ui.display.relation.manager.RelationManagerUI;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Styled JPanel for managing organization members.
 */

public class OrganizationMemberPanel extends JPanel {

    private final String trashIconUrl = "src/main/resources/assets/icons/trash-03.png";
    private final String addAudienceUrl = "src/main/resources/assets/icons/users-plus.png";
    private final QuickActionButton manageCampaignsButton;
    private final QuickActionButton deleteButton = new QuickActionButton("Delete", trashIconUrl, this::deleteSelectedUser);

    private CampaignManager selectedManager;
    private final Organization dashboard;

    /**
     * Sets parent dashboard, initializes the layout and UI, sets button callbacks
     * @param dashboard parent dashboard
     */

    public OrganizationMemberPanel(Organization dashboard) {
        this.dashboard = dashboard;


        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(layout);

        JLabel sectionTitle = new JLabel("Quick Actions");

        JLabel actionTitle = new JLabel("Manage User");
        sectionTitle.putClientProperty( "FlatLaf.styleClass", "h1" );
        actionTitle.putClientProperty( "FlatLaf.styleClass", "h1" );


        manageCampaignsButton = new QuickActionButton("Manage Campaigns", () -> {

            Map<String, Boolean> campaignRelationList = new HashMap<>();

            List<String> campaignNameList = new ArrayList<>();
            try {
                ConnectionService.getCampaignsOfCampaignManager(selectedManager.getUserName()).forEach(i -> {
                    campaignRelationList.put(i.getName(), true);
                    campaignNameList.add(i.getName());
                });
                ConnectionService.getCampaigns().forEach(i -> { if(campaignNameList.stream().noneMatch(x -> x.equals(i.getName()))) {
                    campaignRelationList.put(i.getName(), false);
                }

                });
            } catch (SQLException ignored) { }

            RelationManagerUI ui = new RelationManagerUI("Audiences", campaignRelationList, this::deleteCampaignConnection, this::addCampaignConnection);
            ui.setVisible(true);

        });


        deleteButton.setBackground(new Color(255, 71, 71, 92));

        deleteButton.setEnabled(false);
        manageCampaignsButton.setEnabled(false);

        this.add(Box.createVerticalStrut(20));
        this.add(sectionTitle);
        this.add(Box.createVerticalStrut(10));

        String plusIconUrl = "src/main/resources/assets/icons/plus-square.png";
        this.add(new QuickActionButton("Add Member", plusIconUrl, () -> {
            UserComposerUI ui = new UserComposerUI(dashboard);
            ui.setVisible(true);
        }));
        this.add(Box.createVerticalStrut(40));

        this.add(actionTitle);
        this.add(Box.createVerticalStrut(10));

        this.add(manageCampaignsButton);
        this.add(Box.createVerticalStrut(10));
        this.add(deleteButton);
    }

    /**
     * Shows quick actions for selected campaign manager. Quick action options vary by user type.
     * @param cm selected campaign manager
     * @throws SQLException
     */

    public void showUserQuickActions(CampaignManager cm) throws SQLException {

        this.selectedManager = cm;

        JPanel userActionsPanel = new JPanel();
        GridLayout actionsPanelLayout = new GridLayout(3, 2);
        actionsPanelLayout.setHgap(10);
        actionsPanelLayout.setVgap(10);

        userActionsPanel.setLayout(actionsPanelLayout);

        if(cm.getDisplayMode().equals(DisplayMode.RESPONSIBLE)) {
            manageCampaignsButton.setEnabled(true);
            deleteButton.setEnabled(true);
        } else {
            manageCampaignsButton.setEnabled(false);
            deleteButton.setEnabled(!cm.getUserName().equals("root"));
        }

        this.revalidate();
        this.repaint();
    }

    /**
     * Calls ConnectionService to delete selected campaign - campaign responsible connection
     * @param campaignName campaign of the connection
     */
    private void deleteCampaignConnection(String campaignName) {
        try {
            ConnectionService.removeCampaignFromResponsible(campaignName, selectedManager.getUserName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a Campaign Responsible - Campaign connection for the selected campaign
     * @param campaignName name of the campaign
     */
    private void addCampaignConnection(String campaignName) {
        try {
            ConnectionService.addCampaignToResponsible(campaignName, selectedManager.getUserName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes selected organization member from database if not root.
     */
    private void deleteSelectedUser() {
        try {
            ConnectionService.deleteUser(this.selectedManager.getUserName());
        } catch (SQLException ignored) { }
        this.dashboard.refreshContent();
    }
}
