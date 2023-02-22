package com.contentwire.ui.display.dashboard.components.elements.panels;

import com.contentwire.model.Audience;
import com.contentwire.model.Campaign;
import com.contentwire.service.repository.service.ConnectionService;
import com.contentwire.service.resource.management.UserManagementService;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmailConfigPanel extends JPanel {

    private JTextField subjectField;
    private JComboBox<String> toAudience;
    private List<Audience> availableAudiences;
    private JLabel subjectMessage;
    private JLabel toLabel;
    private JLabel subjectLabel;
    private JLabel messageLabel;
    private boolean hasAvailableAudience = false;

    /**
     * A styled JPanel for configuring E-Mail data.
     */

    public EmailConfigPanel() {
        createComponents();

        this.add(leftJustify(subjectLabel));
        this.add(Box.createVerticalStrut(15));
        this.add(leftJustify(subjectField));
        this.add(leftJustify(subjectMessage));
        this.add(Box.createVerticalStrut(30));
        this.add(leftJustify(toLabel));
        this.add(Box.createVerticalStrut(15));
        this.add(leftJustify(toAudience));
        this.add(Box.createVerticalStrut(30));
        this.add(leftJustify(messageLabel));
        this.add(Box.createVerticalStrut(30));
    }

    public void setSubjectMessage(String message) {
        subjectMessage.setText(message);
    }

    /**
     * Retrieves audiences available for E-Mailing.
     * @return the audiences available for E-Mailing
     */
    private java.util.List<Audience> getAvailableAudiences() {
        java.util.List<Audience> audiences = new ArrayList<>();
        try {
            List<Campaign> campaigns = ConnectionService.getCampaignsOfCampaignManager(UserManagementService.getCurrentUser().getUserName());
            campaigns.forEach(c -> {
                try {
                    audiences.addAll(ConnectionService.getAudiencesForCampaign(c.getName()));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return audiences;
    }

    /**
     * Left-Aligns JComponents
     * @param component component to align
     * @return left-aligned component
     */

    private Component leftJustify( JComponent component )  {
        Box  b = Box.createHorizontalBox();
        b.add( component );
        b.add( Box.createHorizontalGlue() );
        return b;
    }

    public String getSubject() {
        return this.subjectField.getText();
    }

    /**
     * Initializes and styles Swing components, toggles available audiences.
     */

    private void createComponents() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 700));

        subjectLabel = new JLabel("Subject");
        subjectField = new JTextField();
        toLabel = new JLabel("Audience");
        subjectMessage = new JLabel();
        toAudience = new JComboBox<>();

        availableAudiences = this.getAvailableAudiences();

        if(availableAudiences.size() == 0) {
            toAudience.addItem("No Audiences Available.");
            hasAvailableAudience = false;
            toAudience.setEnabled(false);
        } else {
            toAudience.setEnabled(true);
            hasAvailableAudience = true;
            availableAudiences.forEach(a -> toAudience.addItem(a.getName()));
        }

        messageLabel = new JLabel("Content");
        subjectLabel.putClientProperty( "FlatLaf.styleClass", "h2" );

        toLabel.putClientProperty( "FlatLaf.styleClass", "h2" );
        messageLabel.putClientProperty( "FlatLaf.styleClass", "h2" );

        subjectField.setMaximumSize(new Dimension(800, 40));
        subjectField.setPreferredSize(new Dimension(800, 40));
        toAudience.setMaximumSize(new Dimension(300, 40));
        toAudience.setPreferredSize(new Dimension(300, 40));

        subjectField.setBorder(BorderFactory.createCompoundBorder(
                subjectField.getBorder(),
                BorderFactory.createEmptyBorder(10, 5, 10, 5))
        );

        toAudience.setBorder(BorderFactory.createCompoundBorder(
                toAudience.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5))
        );
    }

    /**
     * Retrieves selected audience
     * @return the audience selected with JCombobox
     */

    public Audience getAudience() {
        for (Audience availableAudience : availableAudiences) {
            if (availableAudience.getName().equals(String.valueOf(toAudience.getSelectedItem()))) {
                return availableAudience;
            }
        }
        return null;
    }

    public boolean hasAvailableAudience() {
        return hasAvailableAudience;
    }

}
