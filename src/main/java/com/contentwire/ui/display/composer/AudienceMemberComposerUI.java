package com.contentwire.ui.display.composer;

import com.contentwire.model.AudienceMember;
import com.contentwire.service.repository.service.ConnectionService;
import com.contentwire.ui.display.dashboard.components.pages.orchestrator.pages.AudienceMembers;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class AudienceMemberComposerUI extends JFrame {
    private JPanel panel;
    private JTextField memberInput;
    private JTextField emailInput;
    private JButton loginBtn;
    private JLabel errorMessage;

    public AudienceMemberComposerUI(AudienceMembers dashboard) {

        this.setTitle("ContentWIRE");
        this.initView();
        this.initLookAndFeel();
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        this.loginBtn.addActionListener(e -> {
            String email = emailInput.getText().toLowerCase();
            String fullName = memberInput.getText();

            String emailRegex = "^[a-zA-Z\\d_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z\\d.-]+$";
            Pattern pattern = Pattern.compile(emailRegex);

            if(!pattern.matcher(email).matches()) {
                errorMessage.setText("<html><font color='#CD5C5C'>Error: Invalid E-Mail Address.</font></html>");
                return;
            }

            if(fullName.equals("") || email.equals("")) {
                errorMessage.setText("<html><font color='#CD5C5C'>Error: Fields cannot be empty.</font></html>");
            } else {
                try {
                    ConnectionService.insertAudienceMember(new AudienceMember(email, fullName));
                    errorMessage.setText("");
                    dashboard.refreshContent();
                    setVisible(false);
                } catch (SQLException ex) {
                    errorMessage.setText("<html><font color='#CD5C5C'>Error: Member with this E-Mail already exists.</font></html>");
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
        JLabel fullNameLabel = new JLabel("<html><font size='4'>Full Name</font></html>");
        JLabel emailLabel = new JLabel("<html><font size='4'>E-Mail Address</font></html>");
        JLabel title = new JLabel("<html><h1><font size='7'>Add Audience Member</font></h1></html>");

        panel = new JPanel();

        panel.setLayout(new GridLayout(11,1));

        initFormView(fullNameLabel, emailLabel, title, panel, emailInput, memberInput, errorMessage);

        panel.add(Box.createVerticalStrut(25));

        panel.add(loginBtn);

    }

    public static void initFormView(JLabel fullNameLabel, JLabel emailLabel, JLabel title, JPanel panel, JTextField emailInput, JTextField memberInput, JLabel errorMessage) {
        panel.setBorder(BorderFactory.createEmptyBorder(40,75,70,75));

        panel.add(title);
        panel.add(Box.createVerticalStrut(25));

        panel.add(fullNameLabel);
        panel.add(memberInput);
        panel.add(emailLabel);
        panel.add(emailInput);
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
        emailInput = new JTextField();
        memberInput = new JTextField();
        loginBtn = new JButton("Add Member");
    }
}
