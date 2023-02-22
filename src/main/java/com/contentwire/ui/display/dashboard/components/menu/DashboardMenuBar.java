package com.contentwire.ui.display.dashboard.components.menu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Styled JMenubar
 */

public class DashboardMenuBar extends JMenuBar {

    public void addMenuTab(MenuTab tab) {
        tab.setBorder(new EmptyBorder(5, 20, 5, 20));
        this.add(tab);
        System.out.println("[INFO] Tab " + tab.getName() + " added to app.");
    }
}
