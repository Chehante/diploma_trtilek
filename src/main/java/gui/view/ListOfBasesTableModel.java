package gui.view;

import Main.Base_1C;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class ListOfBasesTableModel extends AbstractTableModel {

    private int columnCount;
    private String[] columnNames;
    private ArrayList<String[]> listOfRows;

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        String[] strings = listOfRows.get(rowIndex);
        strings[columnIndex] = (String) aValue;
        listOfRows.set(rowIndex, strings);
    }

    public ListOfBasesTableModel(String[] columnNames){
        this.columnCount = columnNames.length;
        this.columnNames = columnNames;
        listOfRows = new ArrayList<String[]>();
        for (int i = 0; i < listOfRows.size(); i++) {
            listOfRows.add(new String[getColumnCount()]);
        }
    }

    @Override
    public int getRowCount() {
        return listOfRows.size();
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return listOfRows.get(rowIndex)[columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void addRow(String[] row){
        String[] newRow = new String[getColumnCount()];
        newRow = row;
        listOfRows.add(newRow);

    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    public void deleteRow(int index){
        listOfRows.remove(index);
    }

}
