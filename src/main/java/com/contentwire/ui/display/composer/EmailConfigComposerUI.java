package com.contentwire.ui.display.composer;

import com.contentwire.model.Audience;
import com.contentwire.model.Message;
import com.contentwire.service.email.management.EmailManagementService;
import com.contentwire.service.email.management.SessionService;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.swing.*;
import java.awt.*;

public class EmailConfigComposerUI extends JFrame{
    private JPanel panel;
    private JTextField smtpServerInput;
    private JTextField addrInput;
    private JTextField portInput;
    private JPasswordField passwordInput;
    private JButton sendBtn;
    private JLabel infoMessage;


    public EmailConfigComposerUI(Audience audience, Message message) {
        this.setTitle("ContentWIRE");
        this.initView();
        this.initLookAndFeel();
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        this.sendBtn.addActionListener(e -> {
            String smtp = smtpServerInput.getText();
            String addr = addrInput.getText();
            String port = portInput.getText();
            String pwd = new String(passwordInput.getPassword());

            if(pwd.equals("") || addr.equals("") || smtp.equals("") || port.equals("")) {
                infoMessage.setText("<html><font color='#CD5C5C'>Error: Fields cannot be empty.</font></html>");
            } else {
                SessionService sessionService = new SessionService(smtp, port);
                sessionService.authenticate(addr, pwd);
                Session session = sessionService.createSession();

                try {
                    EmailManagementService.sendForAudience(message, addr, session, audience);
                    this.setVisible(false);
                } catch (MessagingException ex) {
                    infoMessage.setText("<html><font color='#CD5C5C'>An unexpected error occurred while sending E-Mail.</font></html>");
                }
            }
        });
    }


    private void initView() {
        this.setIconImage((new ImageIcon("src/main/resources/assets/logo.png")).getImage());
        this.setSize(600, 600);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.initComponents();
        this.initPanel();
        this.add(panel);
    }

    private void initPanel() {
        JLabel smtpLabel = new JLabel("<html><font size='4'>SMTP Server</font></html>");
        JLabel addrLabel = new JLabel("<html><font size='4'>E-Mail Address</font></html>");
        JLabel portLabel = new JLabel("<html><font size='4'>SMTP Port</font></html>");
        JLabel passwordLabel = new JLabel("<html><font size='4'>Password</font></html>");
        JLabel title = new JLabel("<html><h1><font size='7'>Send E-Mail</font></h1></html>");

        panel = new JPanel();

        panel.setLayout(new GridLayout(13,1));
        initFormView(smtpLabel, addrLabel, portLabel, passwordLabel, title, panel, smtpServerInput, addrInput, portInput, passwordInput, infoMessage);

        panel.add(Box.createVerticalStrut(25));
        panel.add(sendBtn);

    }

    public static void initFormView(JLabel smtpLabel, JLabel addrLabel, JLabel portLabel, JLabel passwordLabel,
                                    JLabel title, JPanel panel, JTextField smtpServerInput, JTextField addrInput,
                                    JTextField portInput, JPasswordField passwordInput, JLabel errorMessage) {
        panel.setBorder(BorderFactory.createEmptyBorder(40,75,30,75));

        smtpServerInput.setText("smtp.gmail.com");
        portInput.setText("465");

        panel.add(title);
        panel.add(Box.createVerticalStrut(25));
        panel.add(smtpLabel);
        panel.add(smtpServerInput);
        panel.add(portLabel);
        panel.add(portInput);
        panel.add(addrLabel);
        panel.add(addrInput);
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
        infoMessage = new JLabel("");
        smtpServerInput = new JTextField();
        addrInput = new JTextField();
        portInput = new JTextField();
        passwordInput = new JPasswordField();
        sendBtn = new JButton("Send E-Mail");
    }
}
