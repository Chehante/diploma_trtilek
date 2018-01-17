package dao;

import Main.Util_SQL;
import Main.WarningMessage_1C;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MessageService implements MessageDAO, Runnable{

    Connection connection;
    String baseName;
    WarningMessage_1C warningMessage1C;

    public MessageService(WarningMessage_1C warningMessage1C){
        this.warningMessage1C = warningMessage1C;
        this.baseName = warningMessage1C.getBaseName();
        connection = Util_SQL.getConnect(this.baseName);
    }

    @Override
    public void add(WarningMessage_1C warningMessage1C) {
        try {
            Statement statement = connection.createStatement();
            statement.execute("INSERT INTO " + warningMessage1C.getTableName() + " VALUES ('" + warningMessage1C.getMessageText()+ "', '" + warningMessage1C.getIndex() + "')");

            if (statement != null){
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteFirst(WarningMessage_1C warningMessage1C) {
        try {
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM " + warningMessage1C.getTableName());

            if (statement != null){
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public WarningMessage_1C getFirst(String tableName) {
        WarningMessage_1C warningMessage1C = null;
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM " + tableName + " LIMIT 1 ");

            rs.next();
            warningMessage1C = new WarningMessage_1C();
            warningMessage1C.setTableName(tableName);
            warningMessage1C.setMessageText(rs.getString(1));
            warningMessage1C.setIndex(rs.getString(2));
            warningMessage1C.setBaseName(baseName);

            rs.close();

            if (statement != null){
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return warningMessage1C;
    }

    public void closeConnection(){
        if (connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        WarningMessage_1C currentMessage = getFirst(warningMessage1C.getTableName());
        deleteFirst(currentMessage);
        currentMessage.setMessageText(warningMessage1C.getMessageText());
        add(currentMessage);
        closeConnection();
        System.out.println("Оповещение записано в базу: " + warningMessage1C.getBaseName());
    }
}
