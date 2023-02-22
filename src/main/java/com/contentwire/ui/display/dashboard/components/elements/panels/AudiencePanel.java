package com.contentwire.ui.display.dashboard.components.elements.panels;

import com.contentwire.model.*;
import com.contentwire.service.repository.service.ConnectionService;
import com.contentwire.ui.display.composer.AudienceComposerUI;
import com.contentwire.ui.display.dashboard.components.elements.buttons.QuickActionButton;
import com.contentwire.ui.display.dashboard.components.pages.orchestrator.pages.Audiences;
import com.contentwire.ui.display.relation.manager.RelationManagerUI;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Styled JPanel to show actions for selected audiences
 */

public class AudiencePanel extends JPanel {

    private final Audiences dashboard;
    private Audience selectedAudience;
    private final QuickActionButton deleteButton;
    private final QuickActionButton manageMembersButton;

    /**
     * Initialize Swing components, add callback buttons, style components
     * @param dashboard the audience dashboard the callback should arrive to
     */

    public AudiencePanel(Audiences dashboard) {

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
        this.add(new QuickActionButton("Add Audience", plusIconUrl, () -> {
            AudienceComposerUI ui = new AudienceComposerUI(dashboard);
            ui.setVisible(true);
        }));
        this.add(Box.createVerticalStrut(40));

        this.add(actionTitle);
        this.add(Box.createVerticalStrut(10));

        String trashIconUrl = "src/main/resources/assets/icons/trash-03.png";

        deleteButton = new QuickActionButton("Delete", trashIconUrl, this::deleteSelectedAudience);
        deleteButton.setBackground(new Color(255, 71, 71, 92));

        manageMembersButton = new QuickActionButton("Manage Members", () -> {

            Map<String, Boolean> memberRelationList = new HashMap<>();

            List<String> memberList = new ArrayList<>();
            try {
                ConnectionService.getMembersOfAudience(selectedAudience.getName()).forEach(i -> {
                    memberRelationList.put(i.getEmail(), true);
                    memberList.add(i.getEmail());
                });
                ConnectionService.getAudienceMembers().forEach(i -> { if(memberList.stream().noneMatch((x) -> x.equals(i.getEmail()))) {
                    memberRelationList.put(i.getEmail(), false);
                }
                });
            } catch (SQLException e) {
                e.printStackTrace();
            }

            RelationManagerUI ui = new RelationManagerUI("Audiences", memberRelationList, this::deleteAudienceConnection, this::addAudienceConnection);
            ui.setVisible(true);
        });

        manageMembersButton.setEnabled(false);
        deleteButton.setEnabled(false);

        this.add(Box.createVerticalStrut(10));
        this.add(manageMembersButton);
        this.add(Box.createVerticalStrut(10));
        this.add(deleteButton);
    }

    private void addAudienceConnection(String audienceMember) {
        try {
            ConnectionService.setAudienceMembership(selectedAudience.getName(), audienceMember);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteAudienceConnection(String audienceMember) {
        try {
            ConnectionService.removeAudienceMembership(selectedAudience.getName(), audienceMember);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteSelectedAudience() {
        try {
            ConnectionService.deleteAudience(this.selectedAudience.getName());
        } catch (SQLException ignored) { }
        this.dashboard.refreshContent();
    }

    public void showUserQuickActions(Audience audience) {
        this.selectedAudience = audience;

        deleteButton.setEnabled(true);
        manageMembersButton.setEnabled(true);

        JPanel userActionsPanel = new JPanel();
        GridLayout actionsPanelLayout = new GridLayout(3, 2);
        actionsPanelLayout.setHgap(10);
        actionsPanelLayout.setVgap(10);

        userActionsPanel.setLayout(actionsPanelLayout);
    }
}
