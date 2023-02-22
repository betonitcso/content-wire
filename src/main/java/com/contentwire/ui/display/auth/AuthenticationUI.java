package com.contentwire.ui.display.auth;

import com.contentwire.ui.display.composer.UserComposerUI;
import com.formdev.flatlaf.FlatDarkLaf;
import com.contentwire.ui.window.router.exceptions.WrongCredentialsException;
import com.contentwire.service.repository.service.ConnectionService;
import com.contentwire.model.CampaignManager;
import com.contentwire.service.resource.management.UserManagementService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;

/**
 * Authentication window that is responsible for handling the login process for ContentWIRE users.
 */

public class AuthenticationUI extends JFrame{
    private JPanel panel;
    private JTextField userInput;
    private JPasswordField passwordInput;
    private JButton loginBtn;
    private JLabel errorMessage;
    private boolean isAuthenticated = false;

    /**
     * Initializes UI.
     */

    public AuthenticationUI() {
        this.setTitle("ContentWIRE");
        this.initView();
        this.initLookAndFeel();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * Callback for authenticating user. Searches for user in database, authenticates it if found, then closes window.
     */

    private void onLogin() {
        try {
            CampaignManager currentUser = ConnectionService.getUserByName(this.userInput.getText());
            UserManagementService.authenticate(currentUser, this.getPasswordInput());
            errorMessage.setText("");
            UserManagementService.setCurrentUser(currentUser);
            this.isAuthenticated = true;
            this.dispose();
        } catch (WrongCredentialsException | SQLException e) {
            errorMessage.setText("<html><font color='#CD5C5C'>Error: Invalid Credentials.</font></html>");
        }
    }

    /**
     * Initializes window layout.
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
     * Initializes panel layout.
     */

    private void initPanel() {
        JLabel usernameLabel = new JLabel("<html><font size='4'>Username</font></html>");
        JLabel passwordLabel = new JLabel("<html><font size='4'>Password</font></html>");
        JLabel title = new JLabel("<html><h1><font size='7'>Sign in</font></h1></html>");

        panel = new JPanel();

        panel.setLayout(new GridLayout(9,1));
        UserComposerUI.initFormView(usernameLabel, passwordLabel, title, panel, userInput, passwordInput, errorMessage);
        panel.add(Box.createVerticalStrut(15));

        panel.add(loginBtn);

    }

    /**
     * Sets look and feel to FlatLaf.
     */

    private void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel( new FlatDarkLaf() );
        }
        catch (Exception ignored) { }
    }

    /**
     * Initializes Swing components
     */

    private void initComponents() {
        errorMessage = new JLabel("");
        userInput = new JTextField();
        passwordInput = new JPasswordField();
        loginBtn = new JButton("Sign In");

        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLogin();
            }
        });

        passwordInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() == '\n') {
                    onLogin();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    /**
     * Sets window visible when called, stays open until a user is authenticated.
     * @throws InterruptedException
     */

    public void dispatchAuthentication() throws InterruptedException {
        this.setVisible(true);

        while(!this.isAuthenticated) {
            try {
                Thread.sleep(500);
            } catch(InterruptedException ignored) {}
        }

        System.out.println("[INFO] Successfully logged in as '" + UserManagementService.getCurrentUser().getUserName() + "'.");
    }

    private String getPasswordInput() {
        return new String(this.passwordInput.getPassword());
    }
}
