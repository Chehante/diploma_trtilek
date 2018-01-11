package gui.view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class Renderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        Component mcell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        mcell.setBackground(new Color(255, 20, 147));
//        mcell.setBackground(new Color(255, 69, 0));
//        mcell.setBackground(new Color(255, 0, 0));

        return mcell;


    }
}
