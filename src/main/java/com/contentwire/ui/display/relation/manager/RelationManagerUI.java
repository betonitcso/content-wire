package com.contentwire.ui.display.relation.manager;

import com.contentwire.ui.display.dashboard.components.elements.tables.StaticTable;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.ui.FlatButtonBorder;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

public class RelationManagerUI extends JFrame {

    private StaticTable table;
    private String entityName;
    private final DefaultTableModel model = new DefaultTableModel();

    private final JPanel tableContainer = new JPanel();

    /**
     * Universal window for handling relations between ContentWIRE models.
     * @param entityName identifier of the entity to manage
     * @param items items to display in table
     * @param deleteCallback function to call on delete
     * @param addCallback function to call on addition
     */


    public RelationManagerUI(String entityName, Map<String, Boolean> items, DeleteCallback deleteCallback, AddCallback addCallback) {
        super(entityName);

        this.entityName = entityName;

        model.addColumn(entityName);
        model.addColumn("Action");

        this.setTitle("ContentWIRE");

        this.initTable();
        this.fillTable(items);
        this.initView();

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                if(column == 1) {

                    try {
                        String itemName = (String) table.getValueAt(row, 0);
                        if(((String)table.getValueAt(row, 1)).equals("Delete")) {
                            handleDelete(itemName, deleteCallback);
                            model.removeRow(row);
                            model.addRow(new Object[]{itemName, "Add"});
                        } else {
                            handleAdd(itemName, addCallback);
                            model.removeRow(row);
                            model.addRow(new Object[]{itemName, "Delete"});
                        }
                    } catch (Exception ignored) { }

                }
            }
        });


        this.initLookAndFeel();
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }

    private void initView() {
        this.setIconImage((new ImageIcon("src/main/resources/assets/logo.png")).getImage());
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        tableContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        tableContainer.setMinimumSize(new Dimension(400, 500));
        tableContainer.setPreferredSize(new Dimension(400, 500));
        this.add(tableContainer);
    }

    private void initTable() {
        this.table = new StaticTable(model);

        DefaultTableCellRenderer labelRenderer = new DefaultTableCellRenderer();
        labelRenderer.setHorizontalAlignment( JLabel.CENTER );
        table.getColumnModel().getColumn(0).setCellRenderer( labelRenderer );
        table.getColumnModel().getColumn(1).setCellRenderer( labelRenderer );

        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(true);

        table.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        table.setMinimumSize(new Dimension(400, 500));
        table.setPreferredSize(new Dimension(400, 500));

        JScrollPane scrollPane = new JScrollPane(table);

        tableContainer.add(scrollPane);
    }

    private void fillTable(Map<String, Boolean> items) {
        for(Map.Entry<String, Boolean> item: items.entrySet()) {
            if(item.getValue()) {
                model.addRow(new Object[]{item.getKey(), "Delete"});
            } else {
                model.addRow(new Object[]{item.getKey(), "Add"});
            }
        }
    }

    private void handleDelete(String entityID, DeleteCallback deleteCallback) {
        deleteCallback.onDelete(entityID);
    }

    private void handleAdd(String entityID, AddCallback addCallback) {
        addCallback.onAdd(entityID);
    }

    private void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel( new FlatDarkLaf() );
        }
        catch (Exception ignored) { }
    }
}
