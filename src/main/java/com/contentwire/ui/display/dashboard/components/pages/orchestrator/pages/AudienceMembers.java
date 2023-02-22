package com.contentwire.ui.display.dashboard.components.pages.orchestrator.pages;

import com.contentwire.ui.display.dashboard.DashboardUI;
import com.contentwire.ui.display.dashboard.components.elements.panels.AudienceMemberPanel;
import com.contentwire.ui.display.dashboard.components.elements.tables.AudienceMemberTable;
import com.contentwire.ui.display.dashboard.components.pages.Page;

import javax.swing.*;
import java.awt.*;

/**
 * Dashboard for managing Audience Members.
 */

public class AudienceMembers extends Page {
    private final AudienceMemberPanel controlPanel = new AudienceMemberPanel(this);
    private final AudienceMemberTable tablePanel = new AudienceMemberTable();
    public AudienceMembers(String title, DashboardUI window) {
        super(title, window);

        this.addContent(tablePanel);
        this.addContent(controlPanel);

        this.initTableListener();
        this.initContent();
    }

    /**
     * The table synchronizes its state with data in the DB.
     */

    public void refreshContent() {
        this.tablePanel.refresh();}

    /**
     * Initializes event list selection listener for table.
     */
    private void initTableListener() {
        JTable table = tablePanel.getTable();
        table.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            try {
                controlPanel.showUserQuickActions(tablePanel.getSelectedAudienceMember());
            } catch (ArrayIndexOutOfBoundsException ignored) { }
        });
    }

    /**
     * Sets UI style.
     */

    private void initContent() {
        GridLayout contentLayout = new GridLayout(1, 2);
        contentLayout.setHgap(20);
        content.setLayout(contentLayout);
        content.setBorder(BorderFactory.createEmptyBorder(0,0,100,300));
    }
}
