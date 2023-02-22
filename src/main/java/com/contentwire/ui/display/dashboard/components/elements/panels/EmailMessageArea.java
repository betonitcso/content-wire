package com.contentwire.ui.display.dashboard.components.elements.panels;

import com.contentwire.service.resource.management.PreviewManagementService;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * A styled JPanel for entering E-Mail bodies.
 */

public class EmailMessageArea extends JPanel {
    JTextArea messageArea = new JTextArea();

    public EmailMessageArea() {
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 300));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.messageArea.setLineWrap(true);
        this.messageArea.setAutoscrolls(true);
        messageArea.setTabSize(1);

        messageArea.setBorder(BorderFactory.createCompoundBorder(
                messageArea.getBorder(),
                BorderFactory.createEmptyBorder(20, 10, 15, 10))
        );

        messageArea.setMaximumSize(new Dimension(800, 600));
        messageArea.setPreferredSize(new Dimension(800, 600));

        JScrollPane scroll = new JScrollPane (messageArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        this.add(scroll);
    }

    /**
     * Calls function for previewing the HTML-format message.
     */

    public void dispatchPreview() {
        try {
            PreviewManagementService.previewHTML(messageArea.getText());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getBody() {
        return this.messageArea.getText();
    }
}
