package com.contentwire.ui.display.dashboard.components.pages.orchestrator.pages;

import com.contentwire.ui.display.dashboard.DashboardUI;
import com.contentwire.ui.display.dashboard.components.elements.panels.CampaignManagementPanel;
import com.contentwire.ui.display.dashboard.components.elements.tables.CampaignTable;
import com.contentwire.ui.display.dashboard.components.pages.Page;

import javax.swing.*;
import java.awt.*;

/**
 * Dashboard for managing Campaigns of the organization.
 */

public class Campaigns extends Page {

    private CampaignTable tablePanel = new CampaignTable();
    private CampaignManagementPanel controlPanel = new CampaignManagementPanel(this);

    /**
     * Initializes layout and UI
     * @param title title of the page
     * @param window parent window
     */

    public Campaigns(String title, DashboardUI window) {
        super(title, window);

        this.addContent(tablePanel);
        this.addContent(controlPanel);

        this.initTableListener();
        this.initContent();
    }

    /**
     * Synchronizes data with database
     */

    public void refreshContent() {
        this.tablePanel.refresh();
    }

    /**
     * Initializes event listener for table selection
     */

    private void initTableListener() {
        JTable table = tablePanel.getTable();
        table.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            try {
                controlPanel.showUserQuickActions(tablePanel.getSelectedCampaign());
            } catch (ArrayIndexOutOfBoundsException ignored) { }
        });
    }

    /**
     * Initializes layout and UI.ú
     */

    private void initContent() {
        GridLayout contentLayout = new GridLayout(1, 2);
        contentLayout.setHgap(20);
        content.setLayout(contentLayout);
        content.setBorder(BorderFactory.createEmptyBorder(0,0,100,300));
    }
}
