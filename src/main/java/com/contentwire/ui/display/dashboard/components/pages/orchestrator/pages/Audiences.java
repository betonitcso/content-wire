package com.contentwire.ui.display.dashboard.components.pages.orchestrator.pages;

import com.contentwire.ui.display.dashboard.DashboardUI;
import com.contentwire.ui.display.dashboard.components.elements.panels.AudiencePanel;
import com.contentwire.ui.display.dashboard.components.elements.tables.AudienceTable;
import com.contentwire.ui.display.dashboard.components.pages.Page;

import javax.swing.*;
import java.awt.*;

/**
 * Dashboard for managing Audiences.
 */

public class Audiences extends Page {
    private final AudienceTable tablePanel = new AudienceTable();
    private final AudiencePanel controlPanel = new AudiencePanel(this);

    /**
     * Initializes UI.
     * @param title title of page
     * @param window parent window
     */

    public Audiences(String title, DashboardUI window) {
        super(title, window);

        this.addContent(tablePanel);
        this.addContent(controlPanel);

        this.initTableListener();
        this.initContent();
    }


    /**
     *  Synchronizes state with database.
     */

    public void refreshContent() {
        this.tablePanel.refresh();
    }

    /**
     * Initializes event listener for table list selection.
     */
    private void initTableListener() {
        JTable table = tablePanel.getTable();
        table.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            try {
                controlPanel.showUserQuickActions(tablePanel.getSelectedAudience());
            } catch (ArrayIndexOutOfBoundsException ignored) { }
        });
    }

    /**
     * Initializes layout and UI.
     */
    private void initContent() {
        GridLayout contentLayout = new GridLayout(1, 2);
        contentLayout.setHgap(20);
        content.setLayout(contentLayout);
        content.setBorder(BorderFactory.createEmptyBorder(0,0,100,300));
    }
}
