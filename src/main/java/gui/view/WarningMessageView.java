package gui.view;

import Main.Base_1C;
import Main.WarningMessageWorker;
import Main.WarningMessage_1C;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class WarningMessageView extends JPanel {

    WarningMessageWorker warningMessageWorker;

    private static final String PG_DEFAULT_IP_PORT = "5432";
    private static final String LOCALHOST = "127.0.0.1";
    private static final String LIST_OF_MESSAGES = "list warning messages.xml";

    //connection components
    private JLabel serverLable;
    private JTextField serverTextField;
    private JLabel serverPortLable;
    private JTextField serverPortTextField;

    //file of messages components
    private JLabel fileOfMessagesLabel;
    private JTextField fileOfMessagesTextField;

    //buttons components
    private JButton getMessagesListButton;
    private JButton sendMessagesButton;
    private JButton refreshMessageListFileButton;

    //table component
    private JLabel tableOfMessagesLabel;
    private JTable tableOfMessages;
    private JScrollPane jScrollPane;
    private ListOfBasesTableModel listOfMessagesTableModel;
    private JButton addButton;
    private JButton deleteButton;

    public WarningMessageView(WarningMessageWorker warningMessageWorker) {
        this.warningMessageWorker = warningMessageWorker;
        createComponents();
        init();
        setLayout(new BorderLayout());
        addComponents();
    }

    private void createComponents() {
        createFields();
        createTable();
        createMessagesListButton();
        createSendMessagesButton();
        createRefreshMessagesListFileButton();
        createAddButton();
        createDeleteButton();
    }

    private void addComponents(){

        JPanel firstPannel = new JPanel();
        firstPannel.setLayout(new BorderLayout());
        firstPannel.add(fileOfMessagesLabel, BorderLayout.WEST);
        firstPannel.add(fileOfMessagesTextField);
        add(firstPannel, BorderLayout.PAGE_START);

        JPanel secondPanel = new JPanel();
        add(secondPanel, BorderLayout.CENTER);
        secondPanel.setLayout(new BorderLayout());

        JPanel secondPanel1 = new JPanel();
        secondPanel.add(secondPanel1, BorderLayout.NORTH);
        secondPanel1.add(getMessagesListButton);
        secondPanel1.add(sendMessagesButton);
        secondPanel1.add(refreshMessageListFileButton);

        JPanel secondPanel2 = new JPanel();
        secondPanel.add(secondPanel2);
        secondPanel2.add(addButton);
        secondPanel2.add(deleteButton);
        secondPanel2.add(tableOfMessagesLabel);
        secondPanel2.add(jScrollPane);

        JPanel thirdPanel = new JPanel();
        thirdPanel.add(serverLable);
        thirdPanel.add(serverTextField);
        thirdPanel.add(serverPortLable);
        thirdPanel.add(serverPortTextField);
        add(thirdPanel, BorderLayout.PAGE_END);
    }

    private void createMessagesListButton() {
        getMessagesListButton = new JButton("Заполнить сообщения");
        getMessagesListButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {

                try {
                    List<WarningMessage_1C> warningMessages = warningMessageWorker.getListOfWarningMessages(fileOfMessagesTextField.getText());
                    if (warningMessages.size() > 0) {
                        int rowCount = listOfMessagesTableModel.getRowCount();
                        if (rowCount > 0) {
                            for (int i = 0; i < rowCount; i++) {
                                listOfMessagesTableModel.deleteRow(0);
                            }
                        }

                        for (WarningMessage_1C warningMessage: warningMessages) {
                            listOfMessagesTableModel.addRow(new String[]{warningMessage.getBaseName(), warningMessage.getMessageText(), warningMessage.getTableName()});
                            listOfMessagesTableModel.fireTableDataChanged();
                        }

                    }

                } catch (ParserConfigurationException e) {
                    System.out.println("Не удалось распарсить файл: " + fileOfMessagesTextField.getText());
                    e.printStackTrace();
                } catch (IOException e) {
                    System.out.println("Не удалось распарсить файл: " + fileOfMessagesTextField.getText());
                    e.printStackTrace();
                } catch (SAXException e) {
                    System.out.println("Не удалось распарсить файл: " + fileOfMessagesTextField.getText());
                    e.printStackTrace();
                }
            }

        });
    }

    private void createSendMessagesButton() {
        sendMessagesButton = new JButton("Разослать сообщения");
        sendMessagesButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                List<WarningMessage_1C> messageList = getMessageListFromTable();
                if (messageList.size() > 0) {
                    warningMessageWorker.sendingWarningMessages(messageList);
                }
            }

        });
    }

    private List<WarningMessage_1C> getMessageListFromTable(){
        List<WarningMessage_1C> listOfMessages = new LinkedList<WarningMessage_1C>();
        int rowCount = tableOfMessages.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            WarningMessage_1C warningMessage = new WarningMessage_1C();
            warningMessage.setBaseName((String) listOfMessagesTableModel.getValueAt(i, 0));
            warningMessage.setMessageText((String) listOfMessagesTableModel.getValueAt(i, 1));
            warningMessage.setTableName((String) listOfMessagesTableModel.getValueAt(i, 2));
            listOfMessages.add(warningMessage);
        }

        return listOfMessages;
    }

    private void createRefreshMessagesListFileButton() {
        refreshMessageListFileButton = new JButton("Обновить файл");
        refreshMessageListFileButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                List<WarningMessage_1C> messagesList = getMessageListFromTable();
                try {
                    warningMessageWorker.refreshBasesListFile(fileOfMessagesTextField.getText(), messagesList);
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
                    listOfMessagesTableModel.addRow(new String[]{"-", "-", "-"});
                    tableOfMessages.revalidate();
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
                    int selectedRow = tableOfMessages.getSelectedRow();
                    if(selectedRow > -1)
                        listOfMessagesTableModel.deleteRow(selectedRow);
                    tableOfMessages.revalidate();
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

        fileOfMessagesLabel = new JLabel("Файл соообщений:");
        fileOfMessagesTextField = new JTextField();
    }

    private void createTable(){
        tableOfMessagesLabel = new JLabel("Список сообщений для рассылки:");
        String[] columnTitles = {"База", "Сообщение", "Имя таблицы (SQL)"};
        listOfMessagesTableModel = new ListOfBasesTableModel(columnTitles);
        tableOfMessages = new JTable(listOfMessagesTableModel);
        jScrollPane = new JScrollPane(tableOfMessages);
        jScrollPane.setSize(300, 200);
        tableOfMessages.validate();
    }

    private void init() {
        serverTextField.setText(LOCALHOST);
        serverPortTextField.setText(PG_DEFAULT_IP_PORT);
        fileOfMessagesTextField.setText(LIST_OF_MESSAGES);
    }
}
