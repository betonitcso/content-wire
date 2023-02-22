package com.contentwire.ui.display.dashboard.components.elements.panels;

import com.contentwire.model.AudienceMember;
import com.contentwire.service.repository.service.ConnectionService;
import com.contentwire.ui.display.composer.AudienceMemberComposerUI;
import com.contentwire.ui.display.dashboard.components.elements.buttons.QuickActionButton;
import com.contentwire.ui.display.dashboard.components.pages.orchestrator.pages.AudienceMembers;
import com.contentwire.ui.display.relation.manager.RelationManagerUI;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Styled JPanel to show actions for selected audience members
 */

public class AudienceMemberPanel extends JPanel {
    private AudienceMembers dashboard;
    private AudienceMember selectedMember;
    private final QuickActionButton deleteButton;
    private final QuickActionButton manageAudiencesButton;

    /**
     * Initialize Swing components, add callback buttons, style components
     * @param dashboard the dashboard the callback should arrive to
     */

    public AudienceMemberPanel(AudienceMembers dashboard) {

        this.dashboard = dashboard;

        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(layout);

        JLabel sectionTitle = new JLabel("Quick Actions");
        sectionTitle.putClientProperty( "FlatLaf.styleClass", "h1" );

        JLabel actionTitle = new JLabel("Manage Audience Member");
        actionTitle.putClientProperty( "FlatLaf.styleClass", "h1" );

        this.add(Box.createVerticalStrut(20));
        this.add(sectionTitle);
        this.add(Box.createVerticalStrut(10));

        String plusIconUrl = "src/main/resources/assets/icons/plus-square.png";
        this.add(new QuickActionButton("Add Member", plusIconUrl, () -> {
            AudienceMemberComposerUI ui = new AudienceMemberComposerUI(dashboard);
            ui.setVisible(true);
        }));
        this.add(Box.createVerticalStrut(40));

        this.add(actionTitle);
        this.add(Box.createVerticalStrut(10));

        String trashIconUrl = "src/main/resources/assets/icons/trash-03.png";

        deleteButton = new QuickActionButton("Delete", trashIconUrl, this::deleteSelectedMember);
        deleteButton.setBackground(new Color(255, 71, 71, 92));

        manageAudiencesButton = new QuickActionButton("Manage Audiences", () -> {

            Map<String, Boolean> audienceRelationList = new HashMap<>();

            List<String> audienceNameList = new ArrayList<>();
            try {
                ConnectionService.getAudienceMembershipsOfMember(selectedMember.getEmail()).forEach(i -> {
                    audienceRelationList.put(i.getName(), true);
                    audienceNameList.add(i.getName());
                });
                ConnectionService.getAudiences().forEach(i -> { if(audienceNameList.stream().noneMatch(x -> x.equals(i.getName()))) {
                    audienceRelationList.put(i.getName(), false);
                }

                });
            } catch (SQLException ignored) { }

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

    private void addAudienceConnection(String audience) {
        try {
            ConnectionService.setAudienceMembership(audience, selectedMember.getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteAudienceConnection(String audience) {
        try {
            ConnectionService.removeAudienceMembership(audience, selectedMember.getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteSelectedMember() {
        try {
            ConnectionService.deleteAudienceMember(this.selectedMember.getEmail());
        } catch (SQLException ignored) { }
        this.dashboard.refreshContent();
    }

    public void showUserQuickActions(AudienceMember am) {
        this.selectedMember = am;

        deleteButton.setEnabled(true);
        manageAudiencesButton.setEnabled(true);

        JPanel userActionsPanel = new JPanel();
        GridLayout actionsPanelLayout = new GridLayout(3, 2);
        actionsPanelLayout.setHgap(10);
        actionsPanelLayout.setVgap(10);

        userActionsPanel.setLayout(actionsPanelLayout);

    }
}
