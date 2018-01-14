package gui.view;

import Main.Base_1C;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Renderer extends DefaultTableCellRenderer {

    boolean sessionsDenied;
    boolean scheduledJobsDenied;
    int mrow;
    LinkedList<Base_1C> warningBaseList = new LinkedList<Base_1C>();

    public void setWarningBaseList(LinkedList<Base_1C> warningBaseList) {
        this.warningBaseList = warningBaseList;
    }

    public Renderer(){

    }

    public void setMrow(int mrow) {
        this.mrow = mrow;
    }

    public void setSessionsDenied(boolean sessionsDenied) {
        this.sessionsDenied = sessionsDenied;
    }

    public void setScheduledJobsDenied(boolean scheduledJobsDenied) {
        this.scheduledJobsDenied = scheduledJobsDenied;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        Component mcell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        for (Base_1C warningBase:warningBaseList) {
            if ((warningBase.getIndexFromTable() == row)) {
                if (warningBase.isSessionsDenied() && warningBase.isScheduledJobsDenied()) {
                    mcell.setBackground(Color.red);
                } else if (warningBase.isSessionsDenied()){
                    mcell.setBackground(new Color(255, 255, 0));
                } else if (warningBase.isScheduledJobsDenied()) {
                    mcell.setBackground(new Color(255, 20, 147));
                }
                return mcell;
            }
        }

        mcell.setBackground(Color.WHITE);
        return mcell;
        }
}
