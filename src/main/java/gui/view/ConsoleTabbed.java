package gui.view;

import sample.console.ui.view.ClusterListView;
import sample.console.ui.view.ConnectionView;
import sample.console.ui.view.InfoBaseListView;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public final class ConsoleTabbed extends JTabbedPane{

    private NotifierView notifierView;
    private WarningMessageView warningMessageView;

    public ConsoleTabbed(NotifierView notifierView, WarningMessageView warningMessageView) {
        this.notifierView = notifierView;
        this.warningMessageView = warningMessageView;
        add("Рег. задания",notifierView);
        add("Оповещения",warningMessageView);
    }
}
