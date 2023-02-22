package com.contentwire.ui.display.composer;

import com.contentwire.model.Campaign;
import com.contentwire.service.repository.service.ConnectionService;
import com.contentwire.ui.display.dashboard.components.pages.orchestrator.pages.Campaigns;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class CampaignComposerUI extends JFrame {
    private JPanel panel;
    private JTextField nameInput;
    private JTextField descriptionInput;
    private JButton loginBtn;
    private JLabel errorMessage;

    public CampaignComposerUI(Campaigns dashboard) {

        this.setTitle("ContentWIRE");
        this.initView();
        this.initLookAndFeel();
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        this.loginBtn.addActionListener(e -> {
            String campaignName = nameInput.getText();
            String campaignDescription = descriptionInput.getText();

            if(nameInput.equals("")) {
                errorMessage.setText("<html><font color='#CD5C5C'>Error: Campaign Name cannot be empty.</font></html>");
            } else {
                try {
                    ConnectionService.insertCampaign(new Campaign(campaignName, campaignDescription));
                    errorMessage.setText("");
                    dashboard.refreshContent();
                    setVisible(false);
                } catch (SQLException ex) {
                    errorMessage.setText("<html><font color='#CD5C5C'>Error: Audience with this name already exists.</font></html>");
                }
            }
        });
    }


    private void initView() {
        this.setIconImage((new ImageIcon("src/main/resources/assets/logo.png")).getImage());
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.initComponents();
        this.initPanel();
        this.add(panel);
    }

    private void initPanel() {
        JLabel fullNameLabel = new JLabel("<html><font size='4'>Name</font></html>");
        JLabel emailLabel = new JLabel("<html><font size='4'>Description</font></html>");
        JLabel title = new JLabel("<html><h1><font size='7'>Add Campaign</font></h1></html>");

        panel = new JPanel();

        panel.setLayout(new GridLayout(11,1));

        initFormView(fullNameLabel, emailLabel, title, panel, nameInput, descriptionInput, errorMessage);

        panel.add(Box.createVerticalStrut(25));

        panel.add(loginBtn);

    }

    public static void initFormView(JLabel fullNameLabel, JLabel emailLabel, JLabel title, JPanel panel, JTextField nameInput, JTextField descriptionInput, JLabel errorMessage) {
        panel.setBorder(BorderFactory.createEmptyBorder(40,75,70,75));

        panel.add(title);
        panel.add(Box.createVerticalStrut(25));

        panel.add(fullNameLabel);
        panel.add(nameInput);
        panel.add(emailLabel);
        panel.add(descriptionInput);
        panel.add(errorMessage);
    }

    private void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel( new FlatDarkLaf() );
        }
        catch (Exception ignored) { }
    }

    private void initComponents() {
        errorMessage = new JLabel("");
        nameInput = new JTextField();
        descriptionInput = new JTextField();
        loginBtn = new JButton("Add Campaign");
    }
}
