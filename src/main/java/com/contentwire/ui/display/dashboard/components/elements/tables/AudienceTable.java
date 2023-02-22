package com.contentwire.ui.display.dashboard.components.elements.tables;

import com.contentwire.model.Audience;
import com.contentwire.service.repository.service.ConnectionService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.List;

/**
 * Panel for managing Audiences
 */

public class AudienceTable extends JPanel {

    private final StaticTable table;
    private List<Audience> audienceList;
    private DefaultTableModel model = new DefaultTableModel();

    /**
     * Initializes table layout and UI.
     */

    public AudienceTable() {
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
            audienceList  = ConnectionService.getAudiences();
        } catch (SQLException ignored) { }

        for(Audience audience : audienceList) {
            addMember(audience.getName(), audience.getDescription());
        }
    }

    /**
     * Adds audience to the table
     * @param name name of Audience
     * @param description description of Audience
     */

    public void addMember(String name, String description) {
        model.addRow(new Object[]{name, description});
    }

    /**
     *
     * @return selected audience
     */

    public Audience getSelectedAudience() {
        return this.audienceList.get(this.table.getSelectedRow());
    }

    public JTable getTable() {
        return table;
    }

    public DefaultTableModel getModel() {
        return model;
    }
}
