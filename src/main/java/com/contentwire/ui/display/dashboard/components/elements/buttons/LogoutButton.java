package com.contentwire.ui.display.dashboard.components.elements.buttons;

import com.contentwire.ui.display.dashboard.DashboardUI;

import javax.swing.*;
import java.awt.event.WindowEvent;

/**
 * Button for handling user logout
 */

public class LogoutButton extends JButton {

    private DashboardUI dashboard;

    /**
     *
     * @param window window to close
     */

    public LogoutButton(DashboardUI window) {
        super("Log out");

        this.dashboard = window;

        this.addActionListener(e -> {
            dashboard.dispatchEvent(new WindowEvent(dashboard, WindowEvent.WINDOW_CLOSING));
        });
    }
}
