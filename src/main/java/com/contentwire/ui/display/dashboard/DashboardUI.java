package com.contentwire.ui.display.dashboard;

import com.contentwire.service.resource.management.UserManagementService;
import com.contentwire.ui.display.dashboard.components.menu.DashboardMenuBar;
import com.contentwire.ui.display.dashboard.components.menu.MenuController;
import com.contentwire.ui.display.dashboard.components.pages.Page;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;

/**
 * Window for the main ContentWIRE application, it hosts either a Campaign Responsible or a Campaign Orchestrator dashboard.
 */

public class DashboardUI extends JFrame {
    private Page currentPage;

    /**
     * Initialize layout and UI.
     */

    public DashboardUI() {
        super("ContentWIRE");

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.setWindowIcon();
        this.initLookAndFeel();
        this.setWindowSize();
    }

    /**
     * Sets icon of window to ConetentWIRE logo.
     */
    private void setWindowIcon() {
        ImageIcon icon = new ImageIcon("src/main/resources/assets/logo.png");
        this.setIconImage(icon.getImage());
    }

    /**
     * Initializes window size.
     */
    private void setWindowSize() {
        Dimension windowSize = new Dimension((int) (this.getContextResolution().width * 0.8), (int) (this.getContextResolution().height * 0.8));
        this.setSize(windowSize);
        this.setLocationRelativeTo(null);
    }

    /**
     * Gets display size
     * @return the dimensions of the logical display in pixels
     */
    private Dimension getContextResolution() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    /**
     * Sets FlatLaf as default theme if possible.
     */
    private void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        }
        catch (Exception ignored) { }
    }

    /**
     * Adds DashboardMenuBar to the window.
     */
    private void initPage() {
        DashboardMenuBar menuBar = MenuController.generateMenuBar(this, UserManagementService.getCurrentUser());
        this.setJMenuBar(menuBar);
    }

    /**
     * Sets theme properties.
     */
    private void setFlatlafProps() {
        System.setProperty( "flatlaf.menuBarEmbedded", "true" );
    }

    /**
     * Changes current page to the given one
     * @param page new page to display
     */
    public void setPage(Page page) {
        if(this.currentPage != null) {
            this.remove(this.currentPage);
        }
        this.currentPage = page;
        this.add(this.currentPage);
        this.revalidate();
        this.repaint();
    }

    /**
     * The methods sets the window visible and initializes layout.
     */
    public void display() {
        this.setFlatlafProps();
        this.initPage();
        this.setVisible(true);
    }

    public Page getCurrentPage() {
        return this.currentPage;
    }
}
