package gui.view;

import Main.Base_1C;
import Main.Notificator;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.awt.*;

public class NotifierView extends JPanel {

    Notificator notificator;

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
    private JScrollPane jScrollPane;
    private ListOfBasesTableModel listOfBasesTableModel;
    private JButton addButton;
    private JButton deleteButton;

    public NotifierView(Notificator notificator) {
        this.notificator = notificator;
        createComponents();
        init();
        setLayout(new BorderLayout());
        addComponents();
    }

    private void createComponents() {
        createFields();
        createTable();
        createBasesListButton();
        createCheckBasesButton();
        createRefreshBasesListFileButton();
        createAddButton();
        createDeleteButton();
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
        secondPanel.add(secondPanel1, BorderLayout.NORTH);
        secondPanel1.add(getBasesListButton);
        secondPanel1.add(checkBasesButton);
        secondPanel1.add(refreshBasesListFileButton);

        JPanel secondPanel2 = new JPanel();
        secondPanel.add(secondPanel2);
        secondPanel2.add(addButton);
        secondPanel2.add(deleteButton);
        secondPanel2.add(tableOfBasesLabel);
        secondPanel2.add(jScrollPane);

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
                List<Base_1C> basesList = new LinkedList<Base_1C>();
                try {
                    basesList = notificator.getListOfBases(fileOfBasesTextField.getText());
                    if (basesList.size() > 0) {
                        int rowCount = listOfBasesTableModel.getRowCount();
                        if (rowCount > 0) {
                            for (int i = 0; i < rowCount; i++) {
                                listOfBasesTableModel.deleteRow(0);
                            }
                        }

                        for (Base_1C base: basesList) {
                            listOfBasesTableModel.addRow(new String[]{base.getName(), base.getUser(), base.getPassword()});
                            listOfBasesTableModel.fireTableDataChanged();
                        }

                    }

                } catch (ParserConfigurationException e) {
                    System.out.println("Не удалось распарсить файл: " + fileOfBasesTextField.getText());
                    e.printStackTrace();
                } catch (IOException e) {
                    System.out.println("Не удалось распарсить файл: " + fileOfBasesTextField.getText());
                    e.printStackTrace();
                } catch (SAXException e) {
                    System.out.println("Не удалось распарсить файл: " + fileOfBasesTextField.getText());
                    e.printStackTrace();
                }
            }
        });
    }

    private void createCheckBasesButton() {
        checkBasesButton = new JButton("Проверить базы");
        checkBasesButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                List<Base_1C> basesList = getBaseListFromTable();
                if (basesList.size() > 0) {
                    List<Base_1C> warningBases = notificator.checkingOfBases(basesList);

                    if (warningBases.size() > 0) {

                        Renderer renderer = new Renderer();
                        renderer.setWarningBaseList((LinkedList<Base_1C>) warningBases);
                        for (int i = 0; i < 3 ; i++) {
                            tableOfBases.getColumnModel().getColumn(i).setCellRenderer(renderer);
                        }
                        listOfBasesTableModel.fireTableDataChanged();

                    }
                }
            }

        });
    }

    private List<Base_1C> getBaseListFromTable(){
        List<Base_1C> listOfBases = new LinkedList<Base_1C>();
        int rowCount = tableOfBases.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            Base_1C newBase_1C = new Base_1C();
            newBase_1C.setName((String) listOfBasesTableModel.getValueAt(i, 0));
            newBase_1C.setUser((String) listOfBasesTableModel.getValueAt(i, 1));
            newBase_1C.setPassword((String) listOfBasesTableModel.getValueAt(i, 2));
            newBase_1C.setIndexFromTable(i);
            listOfBases.add(newBase_1C);
        }

        return listOfBases;
    }

    private void createRefreshBasesListFileButton() {
        refreshBasesListFileButton = new JButton("Обновить файл");
        refreshBasesListFileButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                List<Base_1C> basesList = getBaseListFromTable();
                try {
                    notificator.refreshBasesListFile(fileOfBasesTextField.getText(), basesList);
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (TransformerException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void createAddButton() {
        addButton = new JButton("Добавить");
        addButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    listOfBasesTableModel.addRow(new String[]{"-", "-", "-"});
                    tableOfBases.revalidate();
                } catch (RuntimeException e) {
                    e.getStackTrace();
                }
            }
        });
    }

    private void createDeleteButton() {
        deleteButton = new JButton("Удалить");
        deleteButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    int selectedRow = tableOfBases.getSelectedRow();
                    if (selectedRow > -1)
                        listOfBasesTableModel.deleteRow(selectedRow);
                    tableOfBases.revalidate();
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

    private void createTable() {
        tableOfBasesLabel = new JLabel("Список баз для проверки:");
        String[] columnTitles = {"База", "Пользователь", "Пароль"};
        listOfBasesTableModel = new ListOfBasesTableModel(columnTitles);
        tableOfBases = new JTable(listOfBasesTableModel);
        jScrollPane = new JScrollPane(tableOfBases);
        jScrollPane.setSize(300, 200);
    }

    private void init() {
        serverTextField.setText(LOCALHOST);
        serverPortTextField.setText(RAS_DEFAULT_IP_PORT);
        fileOfBasesTextField.setText(LIST_OF_BASES_PATH);
    }

}
