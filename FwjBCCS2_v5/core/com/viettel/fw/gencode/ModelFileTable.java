package com.viettel.fw.gencode;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * Created by vtsoft on 4/6/2015.
 */
public class ModelFileTable extends AbstractTableModel {
    private String[] columnNames = {"File path", "File name",
            "Include"};
    private Object[][] data = new Object[800][3];


    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    public Class getColumnClass(int c) {
        //return getValueAt(0, c).getClass();
        if (c == 0) return String.class;
        if (c == 1) return String.class;
        return Boolean.class;

    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        return (col == 2);
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }

    public java.util.List getSelected() {
        java.util.List lst = new ArrayList();
        for (int i = 0; i < currRow; i++) {
            if ((Boolean) data[i][2]) {
                lst.add((String) data[i][1]);
            }
        }

        return lst;
    }

    public void addRow(Object[] values) {
        data[currRow][0] = values[0];
        data[currRow][1] = values[1];
        data[currRow][2] = values[2];

        fireTableRowsInserted(currRow, currRow);
        currRow++;
    }

    public void changeSelectAll(boolean isSelect) {
        for (int i = 0; i < currRow; i++) {
            data[i][2] = isSelect;
        }

        fireTableDataChanged();
    }

    public void removeAll() {
        for (int i = 0; i < currRow; i++) {
            data[i][0] = null;
            data[i][1] = null;
            data[i][2] = null;
        }

        fireTableDataChanged();
        currRow = 0;
    }

    private int currRow = 0;
}
