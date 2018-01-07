package gui.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class NotifierView extends JPanel {

    private static final String RAS_DEFAULT_IP_PORT = "1545";
    private static final String LOCALHOST = "127.0.0.1";
    private static final String LIST_OF_BASES_PATH = "list bases.xml";

    //connection components
    private JLabel serverLable;
    private JTextField serverTextField;
    private JLabel serverPortLable;
    private JTextField serverPortTextField;

    //file of bases components
    private JLabel fileOfBasesLabel;
    private JTextField fileOfBasesTextField;

    //buttons components
    private JButton getBasesListButton;
    private JButton checkBasesButton;
    private JButton refreshBasesListFileButton;

    //table component
    private JLabel tableOfBasesLabel;
    private JTable tableOfBases;

    private TitledBorder panelBorder;

    public NotifierView() {
        createComponents();
        init();
        setLayout(new BorderLayout());
        addComponents();
    }

    public int getPort() {
        try {
            return Integer.valueOf(serverPortTextField.getText()).intValue();
        } catch (NumberFormatException e) {
            throw new RuntimeException("Illegal port value", e);
        }
    }

    public String getServer() {
        return serverTextField.getText();
    }

    private void createComponents() {
        createFields();
        createTable();
        createBasesListButton();
        createCheckBasesButton();
        createRefreshBasesListFileButton();
    }

    private void addComponents(){

        JPanel firstPannel = new JPanel();
        firstPannel.setLayout(new BorderLayout());
        firstPannel.add(fileOfBasesLabel, BorderLayout.WEST);
        firstPannel.add(fileOfBasesTextField);
        add(firstPannel, BorderLayout.PAGE_START);

        JPanel secondPanel = new JPanel();
        add(secondPanel, BorderLayout.CENTER);
        secondPanel.setLayout(new BorderLayout());

        JPanel secondPanel1 = new JPanel();
        secondPanel.add(secondPanel1, BorderLayout.PAGE_START);
        secondPanel1.add(getBasesListButton);
        secondPanel1.add(checkBasesButton);
        secondPanel1.add(refreshBasesListFileButton);

        JPanel secondPanel2 = new JPanel();
        secondPanel.add(secondPanel2);
        secondPanel2.add(tableOfBasesLabel);

        JPanel thirdPanel = new JPanel();
        thirdPanel.add(serverLable);
        thirdPanel.add(serverTextField);
        thirdPanel.add(serverPortLable);
        thirdPanel.add(serverPortTextField);
        add(thirdPanel, BorderLayout.PAGE_END);
    }

    private void createBasesListButton() {
        getBasesListButton = new JButton("Заполнить таблицу");
        getBasesListButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    System.out.println("Обновили таблицу");
                } catch (RuntimeException e) {
                    e.getStackTrace();
                }
            }

        });
    }

    private void createCheckBasesButton() {
        checkBasesButton = new JButton("Проверить базы");
        checkBasesButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    System.out.println("Проверили таблицу");
                } catch (RuntimeException e) {
                    e.getStackTrace();
                }
            }

        });
    }

    private void createRefreshBasesListFileButton() {
        refreshBasesListFileButton = new JButton("Обновить файл");
        refreshBasesListFileButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    System.out.println("Обновили файл");
                } catch (RuntimeException e) {
                    e.getStackTrace();
                }
            }
        });
    }

    private void createFields() {

        serverLable = new JLabel("Server:");
        serverTextField = new JTextField(15);
        serverPortLable = new JLabel("Port:");
        serverPortTextField = new JTextField(5);
        serverPortTextField.setHorizontalAlignment(SwingConstants.RIGHT);

        fileOfBasesLabel = new JLabel("Файл списка баз:");
        fileOfBasesTextField = new JTextField();
    }

    private void createTable(){
        tableOfBasesLabel = new JLabel("Список баз для проверки:");
        tableOfBases = new JTable();
    }

    private void init() {
        serverTextField.setText(LOCALHOST);
        serverPortTextField.setText(RAS_DEFAULT_IP_PORT);
        fileOfBasesTextField.setText(LIST_OF_BASES_PATH);
    }

    private void setBorderTitle(String status) {
        panelBorder.setTitle("Тайтл " + " (" + status + ")");
        repaint();
    }
}
