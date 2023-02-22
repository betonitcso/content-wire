package com.contentwire.ui.display.dashboard.components.pages.responsible.pages;

import com.contentwire.model.Message;
import com.contentwire.ui.display.composer.EmailConfigComposerUI;
import com.contentwire.ui.display.dashboard.DashboardUI;
import com.contentwire.ui.display.dashboard.components.elements.buttons.QuickActionButton;
import com.contentwire.ui.display.dashboard.components.elements.panels.EmailConfigPanel;
import com.contentwire.ui.display.dashboard.components.elements.panels.EmailMessageArea;
import com.contentwire.ui.display.dashboard.components.pages.Page;

import javax.swing.*;
import java.awt.*;

/**
 * Dashboard for sending E-Mails.
 */

public class ResponsibleDashboard extends Page {

    private final EmailConfigPanel emailConfigPanel = new EmailConfigPanel();
    private final EmailMessageArea emailMessageArea = new EmailMessageArea();
    private final JLabel infoMessage = new JLabel();

    /**
     * Initialzies layout, UI, buttons, callbacks, validates messages.
     * @param title title of the page
     * @param window parent window
     */

    public ResponsibleDashboard(String title, DashboardUI window) {
        super(title, window);

        QuickActionButton sendBtn = new QuickActionButton("Send", "src/main/resources/assets/icons/announcement-03.png", () -> {
            String subj = emailConfigPanel.getSubject();
            Message message = new Message(emailConfigPanel.getSubject(), emailMessageArea.getBody());

            if(subj.equals("")) {
                emailConfigPanel.setSubjectMessage("<html><font color='#CD5C5C'>Subject cannot be empty.</font></html>");
                return;
            } else {
                emailConfigPanel.setSubjectMessage("");
            }

            if(emailMessageArea.getBody().equals("")) {
                infoMessage.setText("<html><font color='#CD5C5C'>E-Mail body cannot be empty.</font></html>");
                return;
            } else {
                infoMessage.setText("");
            }

            EmailConfigComposerUI emailConfigComposerUI = new EmailConfigComposerUI(emailConfigPanel.getAudience(), message);
            emailConfigComposerUI.setVisible(true);
        });

        QuickActionButton previewBtn = new QuickActionButton("Preview HTML", "src/main/resources/assets/icons/glasses-02.png", emailMessageArea::dispatchPreview);

        previewBtn.setBackground(new Color(150, 170, 255, 81));

        JPanel buttonWrapper = new JPanel();
        buttonWrapper.setLayout(new FlowLayout(FlowLayout.LEFT));

        buttonWrapper.add(sendBtn);
        buttonWrapper.add(Box.createHorizontalStrut(10));
        buttonWrapper.add(previewBtn);

        if(!emailConfigPanel.hasAvailableAudience()) {
            sendBtn.setEnabled(false);
        }

        this.addContent((JComponent) Box.createVerticalStrut(50));
        this.addContent(emailConfigPanel);
        this.addContent(emailMessageArea);
        this.addContent(leftJustify(infoMessage));
        this.addContent((JComponent) Box.createVerticalStrut(50));
        this.addContent(leftJustify(buttonWrapper));

        initContent();
    }

    /**
     * Initializes content layout
     */

    private void initContent() {
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(0,50,100,0));
    }

    /**
     * Left justifies JComponent.
     * @param component component to left-align
     * @return styled component
     */

    private JComponent leftJustify( JComponent component )  {
        Box  b = Box.createHorizontalBox();
        b.add( component );
        b.add( Box.createHorizontalGlue() );
        return b;
    }
}
