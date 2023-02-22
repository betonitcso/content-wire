package com.contentwire.ui.display.dashboard.components.pages.orchestrator.pages;

import com.contentwire.ui.display.dashboard.DashboardUI;
import com.contentwire.ui.display.dashboard.components.elements.buttons.QuickActionButton;
import com.contentwire.ui.display.dashboard.components.menu.MenuController;
import com.contentwire.ui.display.dashboard.components.pages.Page;

import javax.swing.*;
import java.awt.*;

/**
 * Main dashboard for Campaign Orchestrators
 */

public class Dashboard extends Page {

    /**
     * Initializes UI, buttons and button callbacks
     * @param title title of the page
     * @param window parent window
     */

    public Dashboard(String title, DashboardUI window) {
        super(title, window);

        JLabel quickActions = new JLabel("Quick Actions");
        quickActions.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
        quickActions.putClientProperty( "FlatLaf.styleClass", "h1" );

        JPanel btnContainer = new JPanel();
        GridLayout layout = new GridLayout(4, 2);
        layout.setHgap(20);
        layout.setVgap(20);
        btnContainer.setLayout(layout);
        btnContainer.setBorder(BorderFactory.createEmptyBorder(50, 0, 100, 0));

        btnContainer.add(quickActions);
        btnContainer.add(Box.createVerticalBox());

        btnContainer.add(new QuickActionButton("Manage Campaigns", () -> {
            MenuController.dispatchTabChange(window, "Campaigns");
        }));

        btnContainer.add(new QuickActionButton("Manage Audiences", () -> {
            MenuController.dispatchTabChange(window, "Audiences");
        }));

        btnContainer.add(new QuickActionButton("Manage Audience Members", () -> {
            MenuController.dispatchTabChange(window, "Audience Members");
        }));

        btnContainer.add(new QuickActionButton("Manage Organization Members", () -> {
            MenuController.dispatchTabChange(window, "Organization Members");
        }));

        this.addContent(btnContainer);
        this.initBg();

        initContent();
    }

    /**
     * Sets page illustration.
     */

    private void initBg() {
        String imgPath = "src/main/resources/assets/illustrations/newsletter.png";
        ImageIcon icon = new ImageIcon(new ImageIcon(imgPath).getImage().getScaledInstance(500, 500, Image.SCALE_SMOOTH));
        JLabel bg = new JLabel(icon);
        this.addContent(bg);
    }

    /**
     * Initializes layout.
     */

    private void initContent() {
        GridLayout contentLayout = new GridLayout(1, 1);
        contentLayout.setVgap(20);
        contentLayout.setHgap(20);
        content.setLayout(contentLayout);
        content.setBorder(BorderFactory.createEmptyBorder(100,50,250,0));
    }
}
