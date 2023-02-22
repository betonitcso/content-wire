package com.contentwire.ui.display.composer;

import com.contentwire.model.CampaignOrchestrator;
import com.contentwire.model.CampaignResponsible;
import com.contentwire.service.repository.service.ConnectionService;
import com.contentwire.service.resource.management.UserManagementService;
import com.contentwire.ui.display.dashboard.components.pages.orchestrator.pages.Organization;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class UserComposerUI extends JFrame {

    private JPanel panel;
    private JTextField userInput;
    private JPasswordField passwordInput;
    private JButton loginBtn;
    private JLabel errorMessage;
    private JRadioButton campaignOrchestratorButton;
    private JRadioButton campaignResponsibleButton;
    private ButtonGroup typeButtonGroup;


    public UserComposerUI(Organization dashboard) {


        this.setTitle("ContentWIRE");
        this.initView();
        this.initLookAndFeel();
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        campaignOrchestratorButton.setActionCommand("o");
        campaignResponsibleButton.setActionCommand("r");

        this.loginBtn.addActionListener(e -> {
            String pwd = UserManagementService.genHash(new String(passwordInput.getPassword()));
            String userName = userInput.getText();

            if(new String(passwordInput.getPassword()).equals("") || userName.equals("")) {
                errorMessage.setText("<html><font color='#CD5C5C'>Error: Fields cannot be empty.</font></html>");
            } else {
                try {
                    if(typeButtonGroup.getSelection().getActionCommand().equals("r")) {
                        ConnectionService.insertCampaignResponsible(new CampaignResponsible(userName, pwd));
                    } else {
                        ConnectionService.insertCampaignOrchestrator(new CampaignOrchestrator(userName, pwd, false));
                    }
                    errorMessage.setText("");
                    dashboard.refreshContent();
                    setVisible(false);
                } catch (SQLException ex) {
                    errorMessage.setText("<html><font color='#CD5C5C'>Error: User already exists.</font></html>");
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
        JLabel usernameLabel = new JLabel("<html><font size='4'>Username</font></html>");
        JLabel passwordLabel = new JLabel("<html><font size='4'>Password</font></html>");
        JLabel title = new JLabel("<html><h1><font size='7'>Add User</font></h1></html>");

        campaignResponsibleButton = new JRadioButton("Campaign Responsible", true);
        campaignOrchestratorButton = new JRadioButton("Campaign Orchestrator");

        typeButtonGroup = new ButtonGroup();
        typeButtonGroup.add(campaignOrchestratorButton);
        typeButtonGroup.add(campaignResponsibleButton);

        panel = new JPanel();

        panel.setLayout(new GridLayout(11,1));
        initFormView(usernameLabel, passwordLabel, title, panel, userInput, passwordInput, errorMessage);

        panel.add(campaignOrchestratorButton);
        panel.add(campaignResponsibleButton);

        panel.add(Box.createVerticalStrut(25));

        panel.add(loginBtn);

    }

    public static void initFormView(JLabel usernameLabel, JLabel passwordLabel, JLabel title, JPanel panel, JTextField userInput, JPasswordField passwordInput, JLabel errorMessage) {
        panel.setBorder(BorderFactory.createEmptyBorder(40,75,70,75));

        panel.add(title);
        panel.add(Box.createVerticalStrut(25));

        panel.add(usernameLabel);
        panel.add(userInput);
        panel.add(passwordLabel);
        panel.add(passwordInput);
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
        userInput = new JTextField();
        passwordInput = new JPasswordField();
        loginBtn = new JButton("Add User");
    }
}
