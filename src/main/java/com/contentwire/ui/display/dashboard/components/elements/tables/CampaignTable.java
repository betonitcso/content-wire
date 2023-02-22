package com.contentwire.ui.display.dashboard.components.elements.tables;

import com.contentwire.model.Campaign;
import com.contentwire.service.repository.service.ConnectionService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.List;

/**
 * Panel for managing Audience Members
 */
public class CampaignTable extends JPanel{
    private final StaticTable table;
    private List<Campaign> campaignList;
    private final DefaultTableModel model = new DefaultTableModel();

    /**
     * Initializes table layout and UI.
     */

    public CampaignTable() {
        model.addColumn("Name");
        model.addColumn("Description");

        table = new StaticTable(model);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);

        this.fillTable();

        this.add(table);
        this.add(new JScrollPane(table));
    }

    /**
     * Synchronizes contents with database.
     */
    public void refresh() {
        model.setRowCount(0);
        this.fillTable();
    }

    /**
     * Fills the table from database.
     */
    private void fillTable() {
        try {
            campaignList  = ConnectionService.getCampaigns();
        } catch (SQLException ignored) { }

        for(Campaign campaign : campaignList) {
            addMember(campaign.getName(), campaign.getDescription());
        }
    }

    /**
     * Adds Campaign to the table
     * @param name name of the campaign
     * @param description description of the campaign
     */

    public void addMember(String name, String description) {
        model.addRow(new Object[]{name, description});
    }

    /**
     * Returns item selected inside the table.
     * @return selected Campaign
     */

    public Campaign getSelectedCampaign() {
        return this.campaignList.get(this.table.getSelectedRow());
    }

    public JTable getTable() {
        return table;
    }

    public DefaultTableModel getModel() {
        return model;
    }
}
