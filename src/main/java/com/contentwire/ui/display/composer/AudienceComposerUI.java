package com.contentwire.ui.display.composer;

import com.contentwire.model.Audience;
import com.contentwire.service.repository.service.ConnectionService;
import com.contentwire.ui.display.dashboard.components.pages.orchestrator.pages.Audiences;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Window for adding new audiences to the organization
 */

public class AudienceComposerUI extends JFrame{
    private JPanel panel;
    private JTextField nameInput;
    private JTextField descriptionInput;
    private JButton loginBtn;
    private JLabel errorMessage;

    /**
     * Initializes variables, the layout of the window, sets callback for button press.
     * @param dashboard the source dashboard
     */

    public AudienceComposerUI(Audiences dashboard) {

        this.setTitle("ContentWIRE");
        this.initView();
        this.initLookAndFeel();
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        this.loginBtn.addActionListener(e -> {
            String audienceName = nameInput.getText();
            String audienceDescription = descriptionInput.getText();

            if(audienceName.equals("")) {
                errorMessage.setText("<html><font color='#CD5C5C'>Error: Audience Name cannot be empty.</font></html>");
            } else {
                try {
                    ConnectionService.insertAudience(new Audience(audienceName, audienceDescription));
                    errorMessage.setText("");
                    dashboard.refreshContent();
                    setVisible(false);
                } catch (SQLException ex) {
                    errorMessage.setText("<html><font color='#CD5C5C'>Error: Audience with this name already exists.</font></html>");
                }
            }
        });
    }

    /**
     * Initializes UI elements.
     */
    private void initView() {
        this.setIconImage((new ImageIcon("src/main/resources/assets/logo.png")).getImage());
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.initComponents();
        this.initPanel();
        this.add(panel);
    }

    /**
     * Initializes Swing elements for JPanel
     */

    private void initPanel() {
        JLabel fullNameLabel = new JLabel("<html><font size='4'>Name</font></html>");
        JLabel descriptionLabel = new JLabel("<html><font size='4'>Description</font></html>");
        JLabel title = new JLabel("<html><h1><font size='7'>Add Audience</font></h1></html>");

        panel = new JPanel();

        panel.setLayout(new GridLayout(11,1));

        initFormView(fullNameLabel, descriptionLabel, title, panel, nameInput, descriptionInput, errorMessage);

        panel.add(Box.createVerticalStrut(25));

        panel.add(loginBtn);

    }

    /**
     * Initializes form elements
     * @param fullNameLabel Jlabel for the audience name
     * @param descriptionLabel JLabel for audience description
     * @param title title of window
     */

    public static void initFormView(JLabel fullNameLabel, JLabel descriptionLabel, JLabel title, JPanel panel, JTextField nameInput, JTextField descriptionInput, JLabel errorMessage) {
        panel.setBorder(BorderFactory.createEmptyBorder(40,75,70,75));

        panel.add(title);
        panel.add(Box.createVerticalStrut(25));

        panel.add(fullNameLabel);
        panel.add(nameInput);
        panel.add(descriptionLabel);
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
        loginBtn = new JButton("Add Audience");
    }
}
