package com.contentwire.ui.display.dashboard.components.elements.tables;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Table thats' contents aren't editable when displayed.
 */

public class StaticTable extends JTable {
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public StaticTable(DefaultTableModel model) {
        super(model);
    }
}
