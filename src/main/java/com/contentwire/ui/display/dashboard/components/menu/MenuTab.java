package com.contentwire.ui.display.dashboard.components.menu;

import com.contentwire.ui.display.dashboard.DashboardUI;
import com.contentwire.ui.display.dashboard.components.pages.Page;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Custom JMenu for handling tab change events.
 */

public class MenuTab extends JMenu {
    private final DashboardUI window;
    private final String name;
    private final Page page;

    {
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                window.setPage(page);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                window.setPage(page);
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    /**
     * Initializes UI, adds menu tab to window
     * @param window parent window
     * @param name name of the menu tab
     * @param page the page of the tab
     */

    public MenuTab(DashboardUI window, String name, Page page) {
        super(name);
        this.name = name;
        this.window = window;
        this.page = page;
        MenuController.addPage(page);
    }

    @Override
    public String getName() {
        return name;
    }

}
