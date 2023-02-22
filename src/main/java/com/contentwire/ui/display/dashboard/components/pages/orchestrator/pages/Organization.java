package com.contentwire.ui.display.dashboard.components.pages.orchestrator.pages;

import com.contentwire.ui.display.dashboard.DashboardUI;
import com.contentwire.ui.display.dashboard.components.elements.panels.OrganizationMemberPanel;
import com.contentwire.ui.display.dashboard.components.elements.tables.OrganizationMemberTable;
import com.contentwire.ui.display.dashboard.components.pages.Page;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Dashboard for managing organization members.
 */

public class Organization extends Page {
    OrganizationMemberTable tablePanel = new OrganizationMemberTable();
    OrganizationMemberPanel controlPanel = new OrganizationMemberPanel(this);

    /**
     * Set up layout & UI.
     * @param title title of the page
     * @param window parent window
     */

    public Organization(String title, DashboardUI window) {
        super(title, window);

        this.addContent(tablePanel);
        this.addContent(controlPanel);

        this.initTableListener();
        this.initContent();
    }

    /**
     * Synchronizes state with database.
     */

    public void refreshContent() {
        this.tablePanel.refresh();
    }

    /**
     * Initializes event listener for table selection.
     */
    private void initTableListener() {
        JTable table = tablePanel.getTable();
        table.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            try {
                controlPanel.showUserQuickActions(tablePanel.getSelectedManager());
            } catch (SQLException | ArrayIndexOutOfBoundsException ignored) { }
        });
    }

    /**
     * Initializes layout.
     */
    private void initContent() {
        GridLayout contentLayout = new GridLayout(1, 2);
        contentLayout.setHgap(20);
        content.setLayout(contentLayout);
        content.setBorder(BorderFactory.createEmptyBorder(0,0,100,300));
    }
}
