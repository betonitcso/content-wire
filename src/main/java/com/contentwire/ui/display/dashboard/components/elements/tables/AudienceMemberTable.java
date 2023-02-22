package com.contentwire.ui.display.dashboard.components.elements.tables;

import com.contentwire.model.AudienceMember;
import com.contentwire.service.repository.service.ConnectionService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel for managing Audience Members
 */
public class AudienceMemberTable extends JPanel{

    private StaticTable table;
    List<AudienceMember> audienceMemberList = new ArrayList<>();
    DefaultTableModel model = new DefaultTableModel();

    /**
     * Initializes table layout and UI.
     */

    public AudienceMemberTable() {
        model.addColumn("Full Name");
        model.addColumn("E-Mail Address");

        table = new StaticTable(model);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);

        fillTable();

        this.add(table);
        this.add(new JScrollPane(table));
    }

    /**
     * Fills the table from database.
     */
    private void fillTable() {
        try {
            audienceMemberList  = ConnectionService.getAudienceMembers();
        } catch (SQLException ignored) { }

        for(AudienceMember am : audienceMemberList) {
            addMember(am.getName(), am.getEmail());
        }
    }

    /**
     * Synchronizes contents with database.
     */
    public void refresh() {
        model.setRowCount(0);
        this.fillTable();
    }

    /**
     * Returns item selected inside the table.
     * @return selected Audience Member
     */

    public AudienceMember getSelectedAudienceMember() {
        return this.audienceMemberList.get(this.table.getSelectedRow());
    }

    /**
     * Adds an Audience Member to the table.
     * @param name name of the Audience Member
     * @param email E-Mail address of Audience Member
     */

    public void addMember(String name, String email) { model.addRow(new Object[]{name, email}); }

    /**
     *
     * @return table inside JPanel
     */
    public JTable getTable() {
        return table;
    }

    public DefaultTableModel getModel() {
        return model;
    }
}
