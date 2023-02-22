package com.contentwire.ui.display.dashboard.components.elements.tables;

import com.contentwire.model.CampaignManager;
import com.contentwire.model.DisplayMode;
import com.contentwire.service.repository.service.ConnectionService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel for managing Audience Members containing
 */

public class OrganizationMemberTable extends JPanel {
    private final StaticTable table;
    DefaultTableModel model = new DefaultTableModel();
    List<CampaignManager> campaignManagerList = new ArrayList<>();

    /**
     * Initializes table layout and UI.
     */

    public OrganizationMemberTable() {
        model.addColumn("Username");
        model.addColumn("Role");

        table = new StaticTable(model);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);

        fillTable();

        this.add(table);
        this.add(new JScrollPane(table));
    }

    /**
     * Fills table with data loaded from database.
     */

    private void fillTable() {
        try {
            campaignManagerList  = ConnectionService.getOrganizationMembers();
        } catch (SQLException ignored) { }

        for(CampaignManager cr : campaignManagerList) {
            if(cr.getDisplayMode().equals(DisplayMode.RESPONSIBLE)) {
                addMember(cr.getUserName(), "Responsible");
            } else {
                addMember(cr.getUserName(), "Orchestrator");
            }
        }
    }

    /**
     * Synchronizes content with database.
     */

    public void refresh() {
        model.setRowCount(0);
        this.fillTable();
    }

    /**
     *
     * @return Campaign Manager selected in table
     */

    public CampaignManager getSelectedManager() {
        return this.campaignManagerList.get(this.table.getSelectedRow());
    }

    /**
     * Adds an organization member to the table
     * @param username username of manager
     * @param role role of manager
     */

    public void addMember(String username, String role) {
        model.addRow(new Object[]{username, role});
    }

    public JTable getTable() {
        return table;
    }

    public DefaultTableModel getModel() {
        return model;
    }
}
